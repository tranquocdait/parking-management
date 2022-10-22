package com.huyendieu.parking.controllers;

import com.huyendieu.parking.model.request.SignUpRequestModel;
import com.huyendieu.parking.model.response.UserResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequestModel requestModel) {
        try {
            userService.signup(requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "create user not successfully");
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        try {
            UserResponseModel userResponseModel = userService.getMyProfile(authentication);
            return new ResponseEntity(new SuccessfulResponseModel(userResponseModel), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "authentication don't exit!");
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
