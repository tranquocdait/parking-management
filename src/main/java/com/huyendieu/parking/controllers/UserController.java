package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.entities.RoleUserEntity;
import com.huyendieu.parking.entities.UserEntity;
import com.huyendieu.parking.model.dto.UserLoginDTO;
import com.huyendieu.parking.model.response.ErrorResponseModel;
import com.huyendieu.parking.model.response.SuccessfulResponseModel;
import com.huyendieu.parking.repositories.RoleUserRepository;
import com.huyendieu.parking.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserLoginDTO userAddDTO) {
        try {
            // create admin role
            RoleUserEntity roleUserEntity = RoleUserEntity.builder()
                    .id(new ObjectId())
                    .code(PermissionConstant.RoleCode.ADMIN.getSecurityCode())
                    .name(PermissionConstant.RoleCode.ADMIN.getName())
                    .createdBy("")
                    .createdDate("")
                    .build();
            roleUserRepository.save(roleUserEntity);

            //add admin account
            UserEntity adminEntity = UserEntity.builder()
                    .userName(PermissionConstant.RoleCode.ADMIN.getName())
                    .password(passwordEncoder.encode(userAddDTO.getPassword())) // password = secret123
                    .roleUser(roleUserEntity)
                    .build();
            userRepository.save(adminEntity);
            return new ResponseEntity(new SuccessfulResponseModel("success"), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "create user not successfully");
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
