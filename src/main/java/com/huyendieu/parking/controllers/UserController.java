package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.model.request.SignInRequestModel;
import com.huyendieu.parking.model.request.SignUpRequestModel;
import com.huyendieu.parking.model.response.UserResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.UserService;
import com.huyendieu.parking.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

	@PostMapping("/sign-in")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignInRequestModel requestModel, HttpServletRequest request) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
			HttpEntity<SignInRequestModel> httpEntity = new HttpEntity<>(requestModel);
			ResponseEntity<SuccessfulResponseModel> response = restTemplate.exchange(uri + PermissionConstant.LOGIN_URI,
					HttpMethod.POST, httpEntity, SuccessfulResponseModel.class);
			return new ResponseEntity(response.getBody(), HttpStatus.OK);
		} catch (Exception ex) {
			Map<String, String> errors = new HashMap<>();
			errors.put("message", "create user not successfully");
			return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequestModel requestModel) {
		try {
			Map<String, String> errorMessages = validateSignUp(requestModel);
			if (!CollectionUtils.isEmpty(errorMessages)) {
				return new ResponseEntity(new ErrorResponseModel(errorMessages), HttpStatus.BAD_REQUEST);
			}
			userService.signup(requestModel);
			return new ResponseEntity(new SuccessfulResponseModel(), HttpStatus.OK);
		} catch (Exception ex) {
			Map<String, String> errors = new HashMap<>();
			errors.put("message", ex.getMessage());
			return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/my-profile")
	public ResponseEntity<?> getMyProfile(Authentication authentication) {
		try {
			UserResponseModel userResponseModel = userService.getMyProfile(authentication);
			return new ResponseEntity(new SuccessfulResponseModel(userResponseModel), HttpStatus.OK);
		} catch (Exception ex) {
			Map<String, String> errors = new HashMap<>();
			errors.put("message", ex.getMessage());
			return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
		}
	}

	private Map<String, String> validateSignUp(SignUpRequestModel requestModel) {
		Map<String, String> errorMessages = new HashMap<>();
		if (requestModel.isVehicleOwner() && !ValidateUtils.isDateValid(requestModel.getRegisterDate())) {
			errorMessages.put("register_date", "error format");
		}
		return errorMessages;
	}
}
