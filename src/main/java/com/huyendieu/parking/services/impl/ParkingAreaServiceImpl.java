package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaItemResponseModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.complex.ParkingHistoryComplexRepository;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.utils.DateTimeUtils;
import com.huyendieu.parking.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingAreaServiceImpl implements ParkingAreaService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private ParkingHistoryComplexRepository parkingHistoryComplexRepository;

    @Override
    public String generateQR(String username) {
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            return Constant.Character.BLANK;
        }
        return QRCodeUtils.generateQRCodeImage(parkingAreaEntity.getId().toString());
    }

    @Override
    public TrackingParkingAreaResponseModel trackingManage(Authentication authentication, TrackingParkingRequestModel trackingParkingRequestModel)
    		throws ParkingException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ParkingException("authentication don't exist!");
        }
		String username = authentication.getPrincipal().toString();

		List<TrackingParkingAreaItemResponseModel> trackingParkingAreaModels = new ArrayList<>();
		List<ParkingHistoryEntity> parkingHistoryEntities = parkingHistoryComplexRepository.findAll(username, trackingParkingRequestModel);
		long totalRecord = parkingHistoryComplexRepository.countAll(username, trackingParkingRequestModel);
		if (!CollectionUtils.isEmpty(parkingHistoryEntities)) {
			for (ParkingHistoryEntity parkingHistoryEntity : parkingHistoryEntities) {
				trackingParkingAreaModels.add(mappingHistoryData(parkingHistoryEntity));
			}
		}
        return TrackingParkingAreaResponseModel.builder()
                .dataList(trackingParkingAreaModels)
                .totalRecord(totalRecord)
                .build();
    }

    private TrackingParkingAreaItemResponseModel mappingHistoryData(ParkingHistoryEntity entity) {
        TrackingParkingAreaItemResponseModel model = TrackingParkingAreaItemResponseModel.builder()
                .checkInDate(DateTimeUtils.convertDateTimeFormat(
                        entity.getCheckInDate(),
                        Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                        Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS
                ))
                .checkOutDate(DateTimeUtils.convertDateTimeFormat(
                        entity.getCheckOutDate(),
                        Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                        Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS
                ))
                .build();

        VehicleSummaryEntity vehicleSummaryEntity = entity.getVehicle();
        if (vehicleSummaryEntity != null) {
            model.setPlateNumber(vehicleSummaryEntity.getPlateNumber());
            model.setVehicleModel(vehicleSummaryEntity.getVehicleModel());
            model.setVehicleBrand(vehicleSummaryEntity.getVehicleBrand());
            model.setPlateNumber(vehicleSummaryEntity.getPlateNumber());
            model.setVehicleOwner(vehicleSummaryEntity.getUsernameOwner());
        }
        return model;
    }
}
