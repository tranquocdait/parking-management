package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.response.QRCodeResponseModel;

public interface VehicleService {

    QRCodeResponseModel generateQR(String username) throws ParkingException;
}
