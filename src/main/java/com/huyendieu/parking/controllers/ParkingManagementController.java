package com.huyendieu.parking.controllers;

import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.ParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/parking")
public class ParkingManagementController {

    @Autowired
    private ParkingAreaService parkingAreaService;

    @PostMapping("/tracking-management")
    public ResponseEntity<?> trackingManage(Authentication authentication, @RequestBody TrackingParkingRequestModel trackingParkingRequestModel) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            TrackingParkingAreaResponseModel responseModel = 
            		parkingAreaService.trackingManage(authentication, trackingParkingRequestModel);
			return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
		} catch (Exception ex) {
			Map<String, String> errors = new HashMap<>();
			errors.put("message", ex.getMessage());
			return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
		}
	}
}
