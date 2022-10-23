package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.VehicleService;
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
@RequestMapping("/qr-code")
public class QRCodeController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParkingAreaService parkingAreaService;

    @GetMapping("/generate")
    public ResponseEntity<?> generateQR(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            String authorities = authentication.getAuthorities().toString();
            String QR = Constant.Character.BLANK;
            String username = authentication.getPrincipal().toString();
            if (authorities.contains(PermissionConstant.RoleCode.VEHICLE_OWNER.getCode())) {
                QR = vehicleService.generateQR(username);
            }
            if (authorities.contains(PermissionConstant.RoleCode.PARKING_OWNER.getCode())) {
                QR = parkingAreaService.generateQR(username);
            }
            return new ResponseEntity(new SuccessfulResponseModel(QR), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
