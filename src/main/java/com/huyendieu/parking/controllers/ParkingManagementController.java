package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.model.request.*;
import com.huyendieu.parking.model.response.*;
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
    public ResponseEntity<?> trackingManage(Authentication authentication, @RequestBody TrackingParkingRequestModel requestModel) {
        try {
            TrackingParkingAreaResponseModel responseModel =
                    parkingAreaService.trackingManage(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/vehicle-management")
    public ResponseEntity<?> vehicleManage(Authentication authentication, @RequestBody TrackingParkingRequestModel requestModel) {
        try {
            VehicleManagementResponseModel responseModel =
                    parkingAreaService.vehicleManage(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/parking-registration")
    public ResponseEntity<?> parkingRegistration(Authentication authentication, @RequestBody ParkingRegistrationRequestModel requestModel) {
        try {
            int status = parkingAreaService.parkingRegistration(authentication, requestModel);
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

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribeParkingArea(Authentication authentication, @RequestBody UnsubscribeParkingRequestModel requestModel) {
        try {
            int status = parkingAreaService.unsubscribeParkingArea(authentication, requestModel);
            if (status != 0) {
                return new ResponseEntity(new SuccessfulResponseModel("unsubscribe parking area successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ErrorResponseModel("unsubscribe parking area error!"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/checking-statistics")
    public ResponseEntity<?> checkingStatistics(Authentication authentication, @RequestBody DashboardRequestModel requestModel) {
        try {
            DashboardResponseModel responseModel =
                    parkingAreaService.checkingStatistics(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/income-statistics")
    public ResponseEntity<?> incomeStatistics(Authentication authentication,
            @RequestBody DashboardRequestModel requestModel) {
        try {
            DashboardResponseModel responseModel =
                    parkingAreaService.incomeStatistics(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> getVehicles(Authentication authentication, @RequestBody VehicleRequestModel requestModel) {
        try {
            VehicleListResponseModel responseModel =
                    parkingAreaService.getVehicles(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/capacity-information")
    public ResponseEntity<?> getCapacityInformation(Authentication authentication) {
        try {
            CapacityResponseModel responseModel =
                    parkingAreaService.getCapacityInformation(authentication);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
