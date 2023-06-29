package com.huyendieu.parking.services.common.impl;

import com.huyendieu.parking.entities.UserEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.common.VehicleSummaryService;
import com.huyendieu.parking.utils.MapperUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleSummaryServiceImpl implements VehicleSummaryService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public VehicleSummaryEntity mappingSummaryById(String vehicleId) throws ParkingException {
        Optional<VehicleEntity> optionalVehicle = vehicleRepository.findFirstById(new ObjectId(vehicleId));
        if (optionalVehicle.isEmpty()) {
            return null;
        }
        VehicleEntity vehicleEntity = optionalVehicle.get();
        return _mappingSummaryVehicle(vehicleEntity);
    }

    @Override
    public VehicleSummaryEntity mappingSummaryByUsername(String userName) throws ParkingException {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByActiveIsTrue(userName);
        if (CollectionUtils.isEmpty(vehicleEntities)) {
            return new VehicleSummaryEntity();
        }
        VehicleEntity vehicleEntity = vehicleEntities.get(0);
        return _mappingSummaryVehicle(vehicleEntity);
    }

    private VehicleSummaryEntity _mappingSummaryVehicle(VehicleEntity vehicleEntity) {
        VehicleSummaryEntity vehicleSummaryEntity = MapperUtils.map(vehicleEntity, VehicleSummaryEntity.class);
        UserEntity owner = vehicleEntity.getOwner();
        if (owner != null) {
            vehicleSummaryEntity.setUsernameOwner(owner.getUserName());
        }
        return vehicleSummaryEntity;
    }
}
