package com.huyendieu.parking.services.common.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.services.common.ParkingAreaSummaryService;
import com.huyendieu.parking.utils.MapperUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingAreaSummaryServiceImpl implements ParkingAreaSummaryService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Override
    public ParkingAreaSummaryEntity mappingSummaryById(String parkingAreaId) throws ParkingException {
        Optional<ParkingAreaEntity> optionalParkingAreaEntity = parkingAreaRepository.findFistById(new ObjectId(parkingAreaId));
        if (optionalParkingAreaEntity.isEmpty()) {
            return new ParkingAreaSummaryEntity();
        }
        ParkingAreaEntity parkingAreaEntity = optionalParkingAreaEntity.get();
        return mappingSummary(parkingAreaEntity);
    }

    @Override
    public ParkingAreaSummaryEntity mappingSummaryByUsername(String userName) throws ParkingException {
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(userName);
        if (parkingAreaEntity == null) {
            return new ParkingAreaSummaryEntity();
        }
        return mappingSummary(parkingAreaEntity);
    }

    private ParkingAreaSummaryEntity mappingSummary(ParkingAreaEntity parkingAreaEntity) throws ParkingException {
        ParkingAreaSummaryEntity parkingAreaSummaryEntity = MapperUtils.map(parkingAreaEntity, ParkingAreaSummaryEntity.class);
        parkingAreaSummaryEntity.setUsernameOwner(parkingAreaEntity.getOwner() != null ?
                parkingAreaEntity.getOwner().getUserName() : Constant.Character.BLANK);
        return parkingAreaSummaryEntity;
    }
}
