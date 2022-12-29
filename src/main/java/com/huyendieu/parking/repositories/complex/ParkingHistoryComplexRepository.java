package com.huyendieu.parking.repositories.complex;

import java.util.List;

import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.model.dto.DashboardModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;

public interface ParkingHistoryComplexRepository {

    List<ParkingHistoryEntity> findAll(String userNameOwner, DashboardModel requestModel);

    List<ParkingHistoryEntity> findAllByPaging(String userNameOwner, TrackingParkingRequestModel requestModel);

    long countAll(String userNameOwner, TrackingParkingRequestModel requestModel);
}
