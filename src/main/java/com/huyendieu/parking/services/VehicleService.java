package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.CapacityResponseModel;
import com.huyendieu.parking.model.response.ParkingAreaListResponseModel;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.model.response.TrackingVehicleResponseModel;
import org.springframework.security.core.Authentication;

public interface VehicleService {

    QRCodeResponseModel generateQR(String username) throws ParkingException;

    int parkingRegistration(Authentication authentication, ParkingRegistrationRequestModel requestModel) throws ParkingException;

    TrackingVehicleResponseModel trackingManage(Authentication authentication, TrackingParkingRequestModel requestModel) throws ParkingException;

    CapacityResponseModel getParkingCapacity(String parkingId) throws ParkingException;

    ParkingAreaListResponseModel getParkingAreas();

    void shortedPlateNumber();
}
