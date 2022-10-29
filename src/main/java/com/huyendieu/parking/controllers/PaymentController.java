package com.huyendieu.parking.controllers;

import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/check-in")
    public ResponseEntity<?> checkIn(Authentication authentication, String parkingAreaId) {
        try {
            paymentService.checkIn(authentication, parkingAreaId);
            return new ResponseEntity(new SuccessfulResponseModel(parkingAreaId), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-out")
    public ResponseEntity<?> checkOut(Authentication authentication, String parkingAreaId) {
        try {
            paymentService.checkOut(authentication, parkingAreaId);
            return new ResponseEntity(new SuccessfulResponseModel(parkingAreaId), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
