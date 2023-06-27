package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.constants.NotificationConstant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.notification.NotificationModel;
import com.huyendieu.parking.model.response.CapacityResponseModel;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.ParkingHistoryRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.CheckingService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.services.common.ParkingAreaSummaryService;
import com.huyendieu.parking.utils.MapperUtils;
import com.huyendieu.parking.utils.NotificationUtils;
import com.huyendieu.parking.utils.UserUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CheckingServiceImpl extends BaseService implements CheckingService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingHistoryRepository parkingHistoryRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ParkingAreaService parkingAreaService;

    @Autowired
    private ParkingAreaSummaryService parkingAreaSummaryService;

    @Override
    public CheckParkingResponseModel checkParking(Authentication authentication, String parkingAreaId) throws ParkingException {
        if (!UserUtils.isVehicleRole(authentication)) {
            throw new ParkingException("authentication don't exist!");
        }
        String username = UserUtils.getUserName(authentication);
        List<ParkingHistoryEntity> parkingHistoryEntities =
                parkingHistoryRepository.findUserByNotCheckOut(username, new ObjectId(parkingAreaId));
        if (!CollectionUtils.isEmpty(parkingHistoryEntities)) {
            ParkingHistoryEntity parkingHistoryEntity = checkOut(parkingHistoryEntities.get(0));
            checkingNotification(parkingAreaId, getPlateNumber(parkingHistoryEntity), Constant.CheckParkingCode.CHECK_OUT.getCode());
            return CheckParkingResponseModel.builder()
                    .parkingId(parkingAreaId)
                    .checkType(Constant.CheckParkingCode.CHECK_OUT.getCode())
                    .message(Constant.CheckParkingCode.CHECK_OUT.getValue())
                    .build();
        } else {
            if (!isAvailableCapacity(parkingAreaId)) {
                String messageError = messageSource.getMessage("parking-area-no-longer-available",
                        new Object[]{}, LocaleContextHolder.getLocale());
                throw new ParkingException(messageError);
            }
            ParkingHistoryEntity parkingHistoryEntity = checkIn(parkingAreaId, username);
            checkingNotification(parkingAreaId, getPlateNumber(parkingHistoryEntity), Constant.CheckParkingCode.CHECK_IN.getCode());
            return CheckParkingResponseModel.builder()
                    .parkingId(parkingAreaId)
                    .checkType(Constant.CheckParkingCode.CHECK_IN.getCode())
                    .message(Constant.CheckParkingCode.CHECK_IN.getValue())
                    .build();
        }
    }

    private boolean isAvailableCapacity(String parkingAreaId) {
        Optional<ParkingAreaEntity> optionalParkingAreaEntity = parkingAreaRepository.findFistById(new ObjectId(parkingAreaId));
        if (optionalParkingAreaEntity.isEmpty()) {
            return false;
        }
        ParkingAreaEntity parkingAreaEntity = optionalParkingAreaEntity.get();
        CapacityResponseModel capacityInformation = parkingAreaService.getCapacityByParkingArea(parkingAreaEntity);
        return capacityInformation.getOccupation() <= capacityInformation.getCapacity();
    }

    private ParkingHistoryEntity checkIn(String parkingAreaId, String username) throws ParkingException {
        ParkingAreaSummaryEntity parkingArea = parkingAreaSummaryService.mappingSummary(parkingAreaId);
        VehicleSummaryEntity vehicle = mappingVehicleSummary(username);
        ParkingHistoryEntity parkingHistoryEntity = ParkingHistoryEntity.builder()
                .checkInDate(currentDate())
                .vehicle(vehicle)
                .parkingArea(parkingArea)
                .createdDate(currentDate())
                .createdBy(getClass().getSimpleName())
                .build();
        parkingHistoryRepository.save(parkingHistoryEntity);

        return parkingHistoryEntity;
    }

    private ParkingHistoryEntity checkOut(ParkingHistoryEntity parkingHistoryEntity) throws ParkingException {
        parkingHistoryEntity.setCheckOutDate(currentDate());
        parkingHistoryEntity.setUpdatedDate(currentDate());
        parkingHistoryEntity.setUpdatedBy(getClass().getSimpleName());
        parkingHistoryRepository.save(parkingHistoryEntity);

        return parkingHistoryEntity;
    }

    private void checkingNotification(String parkingAreaId, String plateNumber, String type) throws ParkingException {
        NotificationModel notificationModel = null;
        if (Constant.CheckParkingCode.CHECK_IN.getCode().equals(type)) {
            String message = messageSource.getMessage("check-in-notification",
                    new Object[]{plateNumber}, LocaleContextHolder.getLocale());
            notificationModel = new NotificationModel(NotificationConstant.NotificationType.CHECK_IN.getCode(), message);
        }
        if (Constant.CheckParkingCode.CHECK_OUT.getCode().equals(type)) {
            String message = messageSource.getMessage("check-out-notification",
                    new Object[]{plateNumber}, LocaleContextHolder.getLocale());
            notificationModel = new NotificationModel(NotificationConstant.NotificationType.CHECK_OUT.getCode(), message);
        }
        NotificationUtils.sendNotification(String.format(NotificationConstant.NotificationPath.CHECKING,
                parkingAreaId), notificationModel);
    }

    private String getPlateNumber(ParkingHistoryEntity parkingHistoryEntity) {
        if (parkingHistoryEntity == null) {
            return Constant.Character.BLANK;
        }

        VehicleSummaryEntity vehicle = parkingHistoryEntity.getVehicle();
        if (vehicle == null) {
            return Constant.Character.BLANK;
        }

        return vehicle.getPlateNumber();
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
