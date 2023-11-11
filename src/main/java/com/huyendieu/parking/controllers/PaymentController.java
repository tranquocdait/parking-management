package com.huyendieu.parking.controllers;

import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import com.huyendieu.parking.model.request.UpdatePaymentRequestModel;
import com.huyendieu.parking.model.response.PaymentListResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.PaymentService;
import com.huyendieu.parking.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaymentRequestModel requestModel) {
        try {
            String id = paymentService.create(requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(id), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UpdatePaymentRequestModel requestModel) {
        try {
            String id = paymentService.update(requestModel);
            return new ResponseEntity<>(new SuccessfulResponseModel(id), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication authentication, @PathVariable String id) {
        try {
            if (!UserUtils.isParkingAreaRole(authentication)) {
                return new ResponseEntity<>(new ErrorResponseModel(), HttpStatus.FORBIDDEN);
            }
            String paymentId = paymentService.delete(id);
            return new ResponseEntity<>(new SuccessfulResponseModel(paymentId), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<?> list(Authentication authentication, @Valid @RequestBody SearchPaymentRequestModel requestModel) {
        try {
            String userName = UserUtils.getUserName(authentication);
            boolean isParkingArea = UserUtils.isParkingAreaRole(authentication);
            PaymentListResponseModel responseModel = paymentService.getPayments(requestModel, userName, isParkingArea);
            return new ResponseEntity<>(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/complete/{id}")
    public ResponseEntity<?> complete(@PathVariable String id) {
        try {
            paymentService.complete(id);
            return new ResponseEntity<>(new SuccessfulResponseModel(), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity<>(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
