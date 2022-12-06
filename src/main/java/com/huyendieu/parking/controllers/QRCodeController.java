package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> generateQR(Authentication authentication, @RequestParam("isViewAll") boolean isViewAll) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            String authorities = authentication.getAuthorities().toString();
            QRCodeResponseModel qrCodeResponseModel = null;
            String username = authentication.getPrincipal().toString();
            if (authorities.contains(PermissionConstant.RoleCode.VEHICLE_OWNER.getCode())) {
                qrCodeResponseModel = vehicleService.generateQR(username);
            }
            if (authorities.contains(PermissionConstant.RoleCode.PARKING_OWNER.getCode())) {
                qrCodeResponseModel = parkingAreaService.generateQR(username, isViewAll);
            }
            return new ResponseEntity(new SuccessfulResponseModel(qrCodeResponseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
