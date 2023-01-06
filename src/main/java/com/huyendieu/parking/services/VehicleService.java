package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import org.springframework.security.core.Authentication;

public interface VehicleService {

    QRCodeResponseModel generateQR(String username) throws ParkingException;

    int parkingRegistration(Authentication authentication, ParkingRegistrationRequestModel requestModel) throws ParkingException;
}
