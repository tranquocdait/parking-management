package com.huyendieu.parking.services.common;

import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;

public interface VehicleSummaryService {

    VehicleSummaryEntity mappingSummaryById(String vehicleId) throws ParkingException;

    VehicleSummaryEntity mappingSummaryByUsername(String userName) throws ParkingException;
}
