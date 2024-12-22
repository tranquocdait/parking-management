package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.CapacityResponseModel;
import com.huyendieu.parking.model.response.ParkingAreaListResponseModel;
import com.huyendieu.parking.model.response.TrackingVehicleResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/tracking-management")
    public ResponseEntity<?> trackingManage(Authentication authentication, @RequestBody TrackingParkingRequestModel requestModel) {
        try {
            TrackingVehicleResponseModel responseModel =
                    vehicleService.trackingManage(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/parking/parking-capacity/{parking_id}")
    public ResponseEntity<?> getParkingCapacity(@PathVariable("parking_id") String parkingId) {
        try {
            CapacityResponseModel parkingCapacity = vehicleService.getParkingCapacity(parkingId);
            return new ResponseEntity(new SuccessfulResponseModel(parkingCapacity), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/parking")
    public ResponseEntity<?> getParkingAreas() {
        try {
            ParkingAreaListResponseModel parkingCapacity = vehicleService.getParkingAreas();
            return new ResponseEntity(new SuccessfulResponseModel(parkingCapacity), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shorted-plate-number")
    public ResponseEntity<?> shortedPlateNumber() {
        try {
            vehicleService.shortedPlateNumber();
            return new ResponseEntity(new SuccessfulResponseModel(), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
