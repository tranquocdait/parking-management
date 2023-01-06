package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vehicle")
public class VehicleManagementController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/parking-registration")
    public ResponseEntity<?> parkingRegistration(Authentication authentication, @RequestBody ParkingRegistrationRequestModel requestModel) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            int status = vehicleService.parkingRegistration(authentication, requestModel);
            Constant.ParkingRegistrationStatus registrationStatus = Constant.ParkingRegistrationStatus.findByKey(status);
            if (registrationStatus != null) {
                return new ResponseEntity(new SuccessfulResponseModel(Constant.ParkingRegistrationStatus.findByKey(status).getValue()), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ErrorResponseModel("parking registration error!"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
