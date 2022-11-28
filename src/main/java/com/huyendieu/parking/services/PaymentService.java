package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;

import org.springframework.security.core.Authentication;

public interface PaymentService {

	CheckParkingResponseModel checkParking(Authentication authentication, String parkingAreaId) throws ParkingException;

}
