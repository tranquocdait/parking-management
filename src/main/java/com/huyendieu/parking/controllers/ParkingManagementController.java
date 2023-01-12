package com.huyendieu.parking.controllers;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.model.request.DashboardRequestModel;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.request.UnsubscribeParkingRequestModel;
import com.huyendieu.parking.model.request.base.SearchBaseRequestModel;
import com.huyendieu.parking.model.response.DashboardResponseModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import com.huyendieu.parking.model.response.VehicleListResponseModel;
import com.huyendieu.parking.model.response.VehicleManagementResponseModel;
import com.huyendieu.parking.model.response.base.ErrorResponseModel;
import com.huyendieu.parking.model.response.base.SuccessfulResponseModel;
import com.huyendieu.parking.services.ParkingAreaService;
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
@RequestMapping("/parking")
public class ParkingManagementController {

    @Autowired
    private ParkingAreaService parkingAreaService;

    @PostMapping("/tracking-management")
    public ResponseEntity<?> trackingManage(Authentication authentication, @RequestBody TrackingParkingRequestModel requestModel) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
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
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
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
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
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
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
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
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            DashboardResponseModel responseModel =
                    parkingAreaService.checkingStatistics(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> getVehicles(Authentication authentication, @RequestBody SearchBaseRequestModel requestModel) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity(new ErrorResponseModel("Authentication don't exits!"), HttpStatus.BAD_REQUEST);
            }
            VehicleListResponseModel responseModel =
                    parkingAreaService.getVehicles(authentication, requestModel);
            return new ResponseEntity(new SuccessfulResponseModel(responseModel), HttpStatus.OK);
        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return new ResponseEntity(new ErrorResponseModel(errors), HttpStatus.BAD_REQUEST);
        }
    }
}
