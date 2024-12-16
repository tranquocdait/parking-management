package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.CheckInWithOutPerRequestModel;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;

import org.springframework.security.core.Authentication;

public interface CheckingService {

	CheckParkingResponseModel checkParking(Authentication authentication, String parkingAreaId) throws ParkingException;

    CheckParkingResponseModel checkParkingWithOutPermission(CheckInWithOutPerRequestModel requestModel) throws ParkingException;
}
