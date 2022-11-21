package com.huyendieu.parking.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/check-in/{parkingAreaId}")
    public ResponseEntity<?> checkIn(Authentication authentication, @PathVariable String parkingAreaId) {
        try {
            paymentService.checkIn(authentication, parkingAreaId);
            return new ResponseEntity(new SuccessfulResponseModel(parkingAreaId), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-out/{parkingAreaId}")
    public ResponseEntity<?> checkOut(Authentication authentication, @PathVariable String parkingAreaId) {
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
