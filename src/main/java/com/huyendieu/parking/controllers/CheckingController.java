package com.huyendieu.parking.controllers;

import java.util.HashMap;
import java.util.Map;

import com.huyendieu.parking.model.request.CheckInByParkingAreaRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.huyendieu.parking.model.response.CheckParkingResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.CheckingService;

import javax.validation.Valid;

@RestController
@RequestMapping("/checking")
public class CheckingController {
    @Autowired
    private CheckingService paymentService;

    @GetMapping("{parkingAreaId}")
    public ResponseEntity<?> checkParking(Authentication authentication, @PathVariable String parkingAreaId) {
        try {
        	CheckParkingResponseModel checkParkingResponseModel = paymentService.checkParking(authentication, parkingAreaId);
            return new ResponseEntity<>(new SuccessfulResponseModel(checkParkingResponseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/parking-area")
    public ResponseEntity<?> checkParkingArea(Authentication authentication,
            @Valid @RequestBody CheckInByParkingAreaRequestModel requestModel) {
        try {
            CheckParkingResponseModel checkParkingResponseModel =
                    paymentService.checkParkingByParkingArea(authentication, requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(checkParkingResponseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
