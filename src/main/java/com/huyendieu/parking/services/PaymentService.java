package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import org.springframework.security.core.Authentication;

public interface PaymentService {

    void checkIn(Authentication authentication, String parkingAreaId) throws ParkingException;

    void checkOut(Authentication authentication, String parkingAreaId) throws ParkingException;
}
