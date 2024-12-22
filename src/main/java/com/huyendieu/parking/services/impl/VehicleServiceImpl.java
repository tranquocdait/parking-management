package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.*;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.repositories.complex.VehicleComplexRepository;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.VehicleService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.utils.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl extends BaseService implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private VehicleComplexRepository vehicleComplexRepository;

    @Autowired
    private ParkingAreaService parkingAreaService;

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

    @Override
    public int parkingRegistration(Authentication authentication, ParkingRegistrationRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        validateRequest(requestModel);
        String parkingAreaId = requestModel.getParkingAreaId();
        int status = requestModel.getStatus();
        Optional<ParkingAreaEntity> parkingAreaEntityOptional = parkingAreaRepository.findFistById(new ObjectId(parkingAreaId));
        if (parkingAreaEntityOptional.isEmpty()) {
            throw new ParkingException("Parking area don't exist!");
        }
        ParkingAreaEntity parkingAreaEntity = parkingAreaEntityOptional.get();
        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();

        List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByActiveIsTrue(username);
        if (CollectionUtils.isEmpty(vehicleEntities)) {
            throw new ParkingException("Vehicle don't exist!");
        }
        VehicleEntity vehicleEntity = vehicleEntities.get(0);
        String vehicleId = vehicleEntity.getId().toString();

        VehicleSummaryEntity vehicleSummaryEntity = null;
        if (CollectionUtils.isEmpty(vehicles)) {
            // the first register
            vehicles = new ArrayList<>();
            parkingAreaEntity.setVehicles(vehicles);
        } else {
            for (VehicleSummaryEntity vehicle : vehicles) {
                String vehicleEntityId = vehicle.getId().toString();
                if (vehicleId.equals(vehicleEntityId)) {
                    vehicleSummaryEntity = vehicle;
                }
            }
        }

        if (vehicleSummaryEntity == null) {
            vehicleSummaryEntity = mappingVehicleSummary(vehicleEntity, status);
            vehicles.add(vehicleSummaryEntity);
        } else {
            vehicleSummaryEntity.setStatus(status);
        }

        parkingAreaRepository.save(parkingAreaEntity);
        return status;
    }

    @Override
    public TrackingVehicleResponseModel trackingManage(Authentication authentication, TrackingParkingRequestModel trackingParkingRequestModel)
            throws ParkingException {
        String username = UserUtils.getUserName(authentication);

        List<TrackingVehicleItemResponseModel> trackingVehicleItemResponseModels = new ArrayList<>();
        List<ParkingHistoryEntity> parkingHistoryEntities = vehicleComplexRepository.findAllByPaging(username, trackingParkingRequestModel);
        long totalRecord = vehicleComplexRepository.countAll(username, trackingParkingRequestModel);
        if (!CollectionUtils.isEmpty(parkingHistoryEntities)) {
            for (ParkingHistoryEntity parkingHistoryEntity : parkingHistoryEntities) {
                trackingVehicleItemResponseModels.add(mappingHistoryData(parkingHistoryEntity));
            }
        }
        return TrackingVehicleResponseModel.builder()
                .dataList(trackingVehicleItemResponseModels)
                .totalRecord(totalRecord)
                .build();
    }

    @Override
    public CapacityResponseModel getParkingCapacity(String parkingId) throws ParkingException {
        Optional<ParkingAreaEntity> parkingAreaOptional = parkingAreaRepository.findFistById(new ObjectId(parkingId));
        if (parkingAreaOptional.isPresent()) {
            ParkingAreaEntity parkingAreaEntity = parkingAreaOptional.get();
            CapacityResponseModel responseModel =
                    parkingAreaService.getCapacityByParkingArea(parkingAreaEntity);
            return responseModel;
        }
        throw new ParkingException("Parking area don't exist!");
    }

    @Override
    public ParkingAreaListResponseModel getParkingAreas() {
        List<ParkingAreaEntity> parkingAreaEntities = parkingAreaRepository.findAll();
        List<ParkingAreaResponseModel> dataList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parkingAreaEntities)) {
            for (ParkingAreaEntity parkingAreaEntity : parkingAreaEntities) {
                dataList.add(mappingParkingArea(parkingAreaEntity));
            }
        }
        return ParkingAreaListResponseModel.builder()
                .dataList(dataList)
                .totalRecord(dataList.size())
                .build();
    }

    @Override
    public void shortedPlateNumber() {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        for (VehicleEntity vehicleEntity : vehicleEntities) {
            vehicleEntity.setShortedPlateNumber(
                    PlateNumberUtils.shortedPlateNumber(vehicleEntity.getPlateNumber())
            );
        }

        vehicleRepository.saveAll(vehicleEntities);
    }

    private ParkingAreaResponseModel mappingParkingArea(ParkingAreaEntity parkingAreaEntity) {
        return parkingAreaEntity != null
                ? MapperUtils.map(parkingAreaEntity, ParkingAreaResponseModel.class)
                : new ParkingAreaResponseModel();
    }

    private TrackingVehicleItemResponseModel mappingHistoryData(ParkingHistoryEntity entity) {
        TrackingVehicleItemResponseModel model = TrackingVehicleItemResponseModel.builder()
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

        ParkingAreaSummaryEntity parkingAreaSummaryEntity = entity.getParkingArea();
        if (parkingAreaSummaryEntity != null) {
            model.setParkingId(parkingAreaSummaryEntity.getId().toString());
            model.setPackingOwner(parkingAreaSummaryEntity.getUsernameOwner());
        }
        return model;
    }

    private void validateRequest(ParkingRegistrationRequestModel requestModel) throws ParkingException {
        String parkingAreaId = requestModel.getParkingAreaId();
        List<String> messageErrors = new ArrayList<>();

        if (StringUtils.isEmpty(parkingAreaId)) {
            messageErrors.add("parkingAreaId isn't blank or null!");
        }

        if (!CollectionUtils.isEmpty(messageErrors)) {
            throw new ParkingException(String.join(", ", messageErrors));
        }

        requestModel.setStatus(Constant.ParkingRegistrationStatus.REGISTER.getKey());
    }

    private VehicleSummaryEntity mappingVehicleSummary(VehicleEntity vehicleEntity, int status) {
        if (vehicleEntity == null) {
            return null;
        }
        VehicleSummaryEntity vehicleSummary = MapperUtils.map(vehicleEntity, VehicleSummaryEntity.class);
        vehicleSummary.setUsernameOwner(vehicleEntity.getOwner() != null
                ? vehicleEntity.getOwner().getUserName() : Constant.Character.BLANK);
        vehicleSummary.setRegisterParkingDate(currentDate());
        vehicleSummary.setStatus(status);
        return vehicleSummary;
    }
}
