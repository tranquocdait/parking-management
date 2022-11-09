package com.huyendieu.parking.repositories.complex;

import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;

import java.util.List;

public interface ParkingHistoryComplexRepository {

    List<ParkingHistoryEntity> findAllByNotCheckOut(String userNameOwner, TrackingParkingRequestModel requestModel);

    long countAllByNotCheckOut(String userNameOwner, TrackingParkingRequestModel requestModel);
}
