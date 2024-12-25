package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.constants.NotificationConstant;
import com.huyendieu.parking.entities.*;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.PaymentSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.dto.PaymentInfoModel;
import com.huyendieu.parking.model.request.CheckInByParkingAreaRequestModel;
import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.model.response.CapacityResponseModel;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.ParkingHistoryRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.CheckingService;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.PaymentService;
import com.huyendieu.parking.services.StorageService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.services.common.ParkingAreaSummaryService;
import com.huyendieu.parking.services.common.VehicleSummaryService;
import com.huyendieu.parking.utils.DateTimeUtils;
import com.huyendieu.parking.utils.NotificationUtils;
import com.huyendieu.parking.utils.StringUtils;
import com.huyendieu.parking.utils.UserUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private VehicleSummaryService vehicleSummaryService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    StorageService storageService;

    @Override
    public CheckParkingResponseModel checkParkingByParkingArea(Authentication authentication,
            CheckInByParkingAreaRequestModel requestModel) throws ParkingException {
        String userName = UserUtils.getUserName(authentication);
        Optional<VehicleEntity> vehicleOptional =
                vehicleRepository.findFirstByShortedPlateNumber(requestModel.getShortedPlateNumber());
        if (vehicleOptional.isEmpty()) {
            return CheckParkingResponseModel.builder()
                    .checkType("DONT_EXIST")
                    .message("Plate number don't exist!")
                    .build();
        }
        VehicleEntity vehicleEntity = vehicleOptional.get();
        UserEntity vehicleOwner = vehicleEntity.getOwner();
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(userName);
        return _checkParking(vehicleOwner.getUserName(), parkingAreaEntity.getId().toString(), requestModel.getImageBase64());
    }

    @Override
    public CheckParkingResponseModel checkParking(Authentication authentication, String parkingAreaId) throws ParkingException {
        if (!UserUtils.isVehicleRole(authentication)) {
            throw new ParkingException("authentication don't exist!");
        }
        String username = UserUtils.getUserName(authentication);
        return _checkParking(username, parkingAreaId, null);
    }


    private CheckParkingResponseModel _checkParking(String usernameVehicle, String parkingAreaId, String imageBase64) throws ParkingException {
        List<ParkingHistoryEntity> parkingHistoryEntities =
                parkingHistoryRepository.findUserByNotCheckOut(usernameVehicle, new ObjectId(parkingAreaId));
        CheckParkingResponseModel response = CheckParkingResponseModel.builder()
                .parkingId(parkingAreaId)
                .build();
        ParkingHistoryEntity parkingHistoryEntity;
        Constant.CheckParkingCode checkParkingCode;
        if (!CollectionUtils.isEmpty(parkingHistoryEntities)) {
            // Check out
            checkParkingCode = Constant.CheckParkingCode.CHECK_OUT;
            parkingHistoryEntity = parkingHistoryEntities.get(0);
            PaymentInfoModel paymentInfoModel = paymentService.isPassCheckout(usernameVehicle, parkingAreaId);
            if (paymentInfoModel != null) {
                if (paymentInfoModel.isPass()) {
                    checkOut(parkingHistoryEntity, imageBase64);
                } else {
                    // Waiting For Payment
                    TicketEntity ticket = paymentInfoModel.getTicket();
                    if (ticket != null) {
                        LocalDateTime checkInDate = DateTimeUtils.convertStringToDateTime(
                                parkingHistoryEntity.getCheckInDate(), Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS);
                        float prices = calculatePrices(ticket, checkInDate);
                        response.setPrices(prices);
                        String checkoutDate = currentDate();
                        String paymentId;
                        if (parkingHistoryEntity.getPayment() == null) {
                            // Create Payment
                            PaymentRequestModel paymentModel = new PaymentRequestModel();
                            paymentModel.setParkingAreaId(parkingAreaId);
                            paymentModel.setUsername(usernameVehicle);
                            paymentModel.setTicketId(ticket.getId().toString());
                            paymentModel.setPrices(prices);
                            paymentModel.setStatus(Constant.PaymentStatus.WAITING.getKey());
                            paymentModel.setActive(true);
                            paymentModel.setStartDate(parkingHistoryEntity.getCheckInDate());
                            paymentModel.setEndDate(checkoutDate);
                            paymentId = paymentService.create(paymentModel);

                            PaymentSummaryEntity paymentSummaryEntity = PaymentSummaryEntity.builder()
                                    .id(new ObjectId(paymentId))
                                    .prices(prices)
                                    .build();
                            payment(parkingHistoryEntity, paymentSummaryEntity, imageBase64);
                        } else {
                            PaymentSummaryEntity paymentSummaryEntity = parkingHistoryEntity.getPayment();
                            paymentId = paymentSummaryEntity.getId().toString();
                        }

                        response.setPaymentId(paymentId);
                        response.setCheckOutDate(
                                DateTimeUtils.convertDateTimeFormat(
                                checkoutDate,
                                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS)
                        );
                        checkParkingCode = Constant.CheckParkingCode.WAITING_PAYMENT;
                    }
                }
            }
        } else {
            // Check in
            if (!isAvailableCapacity(parkingAreaId)) {
                String messageError = messageSource.getMessage("parking-area-no-longer-available",
                        new Object[]{}, LocaleContextHolder.getLocale());
                throw new ParkingException(messageError);
            }
            parkingHistoryEntity = checkIn(parkingAreaId, usernameVehicle, imageBase64);
            checkParkingCode = Constant.CheckParkingCode.CHECK_IN;
        }

        String checkInDate = DateTimeUtils.convertDateTimeFormat(
                parkingHistoryEntity.getCheckInDate(),
                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS);
        if (!StringUtils.isEmpty(checkInDate)) {
            response.setCheckInDate(checkInDate);
        }
        String checkOutDate = DateTimeUtils.convertDateTimeFormat(
                parkingHistoryEntity.getCheckOutDate(),
                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS);
        if (!StringUtils.isEmpty(checkOutDate)) {
            response.setCheckOutDate(checkOutDate);
        }
        response.setPlateNumber(getPlateNumber(parkingHistoryEntity));
        response.setCheckType(checkParkingCode.getCode());
        response.setMessage(checkParkingCode.getValue());

        checkingNotification(parkingAreaId, response);

        return response;
    }

    private float calculatePrices(TicketEntity ticket, LocalDateTime checkInDate) {
        float fare = ticket.getFare();
        LocalDateTime checkoutDate = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkoutDate);

        return fare * (daysBetween + 1);
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

    private ParkingHistoryEntity checkIn(String parkingAreaId, String username, String checkInBase64) throws ParkingException {
        ParkingAreaSummaryEntity parkingArea = parkingAreaSummaryService.mappingSummaryById(parkingAreaId);
        VehicleSummaryEntity vehicle = vehicleSummaryService.mappingSummaryByUsername(username);
        String checkInUrl = "";
        if (!StringUtils.isEmpty(checkInBase64)) {
            checkInUrl = storageService.store(checkInBase64);
        }
        ParkingHistoryEntity parkingHistoryEntity = ParkingHistoryEntity.builder()
                .checkInDate(currentDate())
                .vehicle(vehicle)
                .checkInUrl(checkInUrl)
                .parkingArea(parkingArea)
                .createdDate(currentDate())
                .createdBy(getClass().getSimpleName())
                .build();
        parkingHistoryRepository.save(parkingHistoryEntity);

        return parkingHistoryEntity;
    }

    private void checkOut(ParkingHistoryEntity parkingHistoryEntity, String checkOutBase64) throws ParkingException {
        String checkOutUrl = "";
        if (!StringUtils.isEmpty(checkOutBase64)) {
            checkOutUrl = storageService.store(checkOutBase64);
        }
        parkingHistoryEntity.setCheckOutUrl(checkOutUrl);
        parkingHistoryEntity.setCheckOutDate(currentDate());
        parkingHistoryEntity.setUpdatedDate(currentDate());
        parkingHistoryEntity.setUpdatedBy(getClass().getSimpleName());
        parkingHistoryRepository.save(parkingHistoryEntity);
    }

    private void payment(ParkingHistoryEntity parkingHistoryEntity, PaymentSummaryEntity paymentSummaryEntity,
            String checkOutBase64) throws ParkingException {
        String checkOutUrl = "";
        if (!StringUtils.isEmpty(checkOutBase64)) {
            checkOutUrl = storageService.store(checkOutBase64);
        }
        parkingHistoryEntity.setCheckOutUrl(checkOutUrl);
        parkingHistoryEntity.setPayment(paymentSummaryEntity);
        parkingHistoryRepository.save(parkingHistoryEntity);
    }

    private void checkingNotification(String parkingAreaId, CheckParkingResponseModel response) throws ParkingException {
        NotificationUtils.sendNotification(String.format(NotificationConstant.NotificationPath.CHECKING,
                parkingAreaId), response);
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
}
