package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ParkingAreaService {

    String generateQR(String username);

    TrackingParkingAreaResponseModel trackingManage(Authentication authentication, TrackingParkingRequestModel trackingParkingRequestModel) throws ParkingException;
}
