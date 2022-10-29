package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingAreaServiceImpl implements ParkingAreaService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Override
    public String generateQR(String username) {
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            return Constant.Character.BLANK;
        }
        return QRCodeUtils.generateQRCodeImage(parkingAreaEntity.getId().toString());
    }
}
