package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.VehicleService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.utils.MapperUtils;
import com.huyendieu.parking.utils.QRCodeUtils;
import com.huyendieu.parking.utils.StringUtils;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ParkingException("authentication don't exist!");
        }
        String username = authentication.getPrincipal().toString();
        validateRequest(requestModel);
        String vehicleId = requestModel.getVehicleId();
        String parkingAreaId = requestModel.getParkingAreaId();
        int status = requestModel.getStatus();
        Optional<ParkingAreaEntity> parkingAreaEntityOptional = parkingAreaRepository.findFistById(new ObjectId(parkingAreaId));
        if (!parkingAreaEntityOptional.isPresent()) {
            throw new ParkingException("Parking area don't exist!");
        }
        VehicleEntity vehicleEntity = vehicleRepository.findByIdAndUsername(new ObjectId(vehicleId), username);
        if (vehicleEntity == null) {
            throw new ParkingException("Vehicle don't exist!");
        }
        ParkingAreaEntity parkingAreaEntity = parkingAreaEntityOptional.get();

        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();
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

    private void validateRequest(ParkingRegistrationRequestModel requestModel) throws ParkingException {
        String vehicleId = requestModel.getVehicleId();
        String parkingAreaId = requestModel.getParkingAreaId();
        List<String> messageErrors = new ArrayList<>();
        if (StringUtils.isEmpty(vehicleId)) {
            messageErrors.add("vehicleId isn't blank or null!");
        }

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
