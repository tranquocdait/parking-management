package com.huyendieu.parking.services.common;

import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;

public interface ParkingAreaSummaryService {

    ParkingAreaSummaryEntity mappingSummaryById(String parkingAreaId) throws ParkingException;

    ParkingAreaSummaryEntity mappingSummaryByUsername(String userName) throws ParkingException;
}
