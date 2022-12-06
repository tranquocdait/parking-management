package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.VehicleService;
import com.huyendieu.parking.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public QRCodeResponseModel generateQR(String username) throws ParkingException {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByActiveIsTrue(username);
        if (CollectionUtils.isEmpty(vehicleEntities)) {
            throw new ParkingException("authentication don't exist!");
        }
        VehicleEntity vehicleEntity = vehicleEntities.get(0);
        String vehicleId = vehicleEntity.getId().toString();
        return QRCodeResponseModel.builder()
                .userId(vehicleId)
                .qrCode(QRCodeUtils.generateQRCodeImage(vehicleId))
                .build();
    }
}
