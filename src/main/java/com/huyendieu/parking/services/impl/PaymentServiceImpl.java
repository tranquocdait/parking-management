package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.ParkingHistoryRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.PaymentService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.utils.MapperUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

	@Autowired
	private ParkingHistoryRepository parkingHistoryRepository;

	@Override
	public CheckParkingResponseModel checkParking(Authentication authentication, String parkingAreaId) throws ParkingException {
		if (authentication.getPrincipal() == null) {
			throw new ParkingException("authentication don't exist!");
		}
		String username = (String) authentication.getPrincipal();
		List<ParkingHistoryEntity> parkingHistoryEntities = 
				parkingHistoryRepository.findUserByNotCheckOut(username, new ObjectId(parkingAreaId));
		if (!CollectionUtils.isEmpty(parkingHistoryEntities)) {
			checkOut(parkingHistoryEntities.get(0));
			
			return CheckParkingResponseModel.builder()
					.parkingId(parkingAreaId)
					.checkType(Constant.CheckParkingCode.CHECK_OUT.getCode())
					.message(Constant.CheckParkingCode.CHECK_OUT.getValue())
					.build();
		} else {
			checkIn(parkingAreaId, username);

			return CheckParkingResponseModel.builder()
					.parkingId(parkingAreaId)
					.checkType(Constant.CheckParkingCode.CHECK_IN.getCode())
					.message(Constant.CheckParkingCode.CHECK_IN.getValue())
					.build();
		}
	}


    public void checkIn(String parkingAreaId, String username) throws ParkingException {
    	ParkingAreaSummaryEntity parkingArea = mappingParkingAreaSummary(parkingAreaId);
        VehicleSummaryEntity vehicle = mappingVehicleSummary(username);
        ParkingHistoryEntity parkingHistoryEntity = ParkingHistoryEntity.builder()
                .checkInDate(currentDate())
                .vehicle(vehicle)
                .parkingArea(parkingArea)
                .createdDate(currentDate())
                .createdBy(getClass().getSimpleName())
                .build();
        parkingHistoryRepository.save(parkingHistoryEntity);
    }

    public void checkOut(ParkingHistoryEntity parkingHistoryEntity) throws ParkingException {
        parkingHistoryEntity.setCheckOutDate(currentDate());
        parkingHistoryEntity.setUpdatedDate(currentDate());
        parkingHistoryEntity.setUpdatedBy(getClass().getSimpleName());
        parkingHistoryRepository.save(parkingHistoryEntity);
    }

    private ParkingAreaSummaryEntity mappingParkingAreaSummary(String parkingAreaId) {
        Optional<ParkingAreaEntity> optionalParkingAreaEntity = parkingAreaRepository.findFistById(new ObjectId(parkingAreaId));
        if (!optionalParkingAreaEntity.isPresent()) {
            return new ParkingAreaSummaryEntity();
        }
        ParkingAreaEntity parkingAreaEntity = optionalParkingAreaEntity.get();
        ParkingAreaSummaryEntity parkingAreaSummaryEntity = MapperUtils.map(parkingAreaEntity, ParkingAreaSummaryEntity.class);
        parkingAreaSummaryEntity.setUsernameOwner(parkingAreaEntity.getOwner() != null ?
                parkingAreaEntity.getOwner().getUserName() : Constant.Character.BLANK);
        return parkingAreaSummaryEntity;
    }

    private VehicleSummaryEntity mappingVehicleSummary(String username) {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByActiveIsTrue(username);
        if (CollectionUtils.isEmpty(vehicleEntities)) {
            return new VehicleSummaryEntity();
        }
        VehicleEntity vehicleEntity = vehicleEntities.get(0);
        VehicleSummaryEntity vehicleSummaryEntity = MapperUtils.map(vehicleEntity, VehicleSummaryEntity.class);
        vehicleSummaryEntity.setUsernameOwner(username);
        return vehicleSummaryEntity;
    }
}
