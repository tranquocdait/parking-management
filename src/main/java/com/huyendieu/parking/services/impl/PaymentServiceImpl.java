package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.PaymentEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.TicketSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import com.huyendieu.parking.model.request.UpdatePaymentRequestModel;
import com.huyendieu.parking.model.response.PaymentItemResponseModel;
import com.huyendieu.parking.model.response.PaymentListResponseModel;
import com.huyendieu.parking.repositories.PaymentRepository;
import com.huyendieu.parking.repositories.complex.PaymentComplexRepository;
import com.huyendieu.parking.services.PaymentService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.services.common.ParkingAreaSummaryService;
import com.huyendieu.parking.services.common.TicketSummaryService;
import com.huyendieu.parking.services.common.VehicleSummaryService;
import com.huyendieu.parking.utils.DateTimeUtils;
import com.huyendieu.parking.utils.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private VehicleSummaryService vehicleSummaryService;

    @Autowired
    private TicketSummaryService ticketSummaryService;

    @Autowired
    private ParkingAreaSummaryService parkingAreaSummaryService;

    @Autowired
    private PaymentComplexRepository paymentComplexRepository;

    @Override
    public String create(PaymentRequestModel requestModel) throws ParkingException {
        TicketSummaryEntity ticketSummaryEntity = ticketSummaryService.mappingSummaryById(requestModel.getTicketId());
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .vehicle(vehicleSummaryService.mappingSummaryById(requestModel.getVehicleId()))
                .ticket(ticketSummaryEntity)
                .parkingArea(parkingAreaSummaryService.mappingSummaryById(requestModel.getParkingAreaId()))
                .startDate(DateTimeUtils.convertDateFormat(
                        requestModel.getStartDate(),
                        Constant.DateTimeFormat.DD_MM_YYYY,
                        Constant.DateTimeFormat.YYYY_MM_DD))
                .endDate(_prepareEndDate(ticketSummaryEntity, requestModel.getStartDate()))
                .active(false)
                .build();
        paymentRepository.save(paymentEntity);

        return paymentEntity.getId().toString();
    }

    @Override
    public String update(UpdatePaymentRequestModel requestModel) throws ParkingException {
        String id = requestModel.getId();
        Optional<PaymentEntity> optionalPayment = paymentRepository.findFirstById(new ObjectId(id));
        if (optionalPayment.isEmpty()) {
            String messageError = messageSource.getMessage("data-does-not-exist",
                    new Object[]{id}, LocaleContextHolder.getLocale());
            throw new ParkingException(messageError);
        }
        PaymentEntity paymentEntity = optionalPayment.get();
        paymentEntity.setStartDate(DateTimeUtils.convertDateFormat(
                requestModel.getStartDate(),
                Constant.DateTimeFormat.DD_MM_YYYY,
                Constant.DateTimeFormat.YYYY_MM_DD));
        paymentEntity.setEndDate(_prepareEndDate(paymentEntity.getTicket(), requestModel.getStartDate()));
        paymentRepository.save(paymentEntity);

        return paymentEntity.getId().toString();
    }

    @Override
    public String delete(String id) throws ParkingException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findFirstById(new ObjectId(id));
        if (optionalPayment.isEmpty()) {
            String messageError = messageSource.getMessage("data-does-not-exist",
                    new Object[]{id}, LocaleContextHolder.getLocale());
            throw new ParkingException(messageError);
        }
        PaymentEntity paymentEntity = optionalPayment.get();
        String paymentId = paymentEntity.getId().toString();
        paymentRepository.delete(paymentEntity);

        return paymentId;
    }

    @Override
    public void complete(String id) throws ParkingException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findFirstById(new ObjectId(id));
        if (optionalPayment.isEmpty()) {
            String messageError = messageSource.getMessage("data-does-not-exist",
                    new Object[]{id}, LocaleContextHolder.getLocale());
            throw new ParkingException(messageError);
        }
        PaymentEntity paymentEntity = optionalPayment.get();
        paymentEntity.setActive(true);
        paymentRepository.save(paymentEntity);
    }

    @Override
    public PaymentListResponseModel getPayments(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea) {
        List<PaymentItemResponseModel> ticketItemResponseModels = new ArrayList<>();
        List<PaymentEntity> paymentEntities = paymentComplexRepository.findAllByPaging(requestModel, userName, isParkingArea);
        long totalRecord = paymentComplexRepository.countAll(requestModel, userName, isParkingArea);

        if (!CollectionUtils.isEmpty(paymentEntities)) {
            for (PaymentEntity paymentEntity : paymentEntities) {
                ticketItemResponseModels.add(_mappingPaymentData(paymentEntity));
            }
        }
        return PaymentListResponseModel.builder()
                .dataList(ticketItemResponseModels)
                .totalRecord(totalRecord)
                .build();
    }

    private PaymentItemResponseModel _mappingPaymentData(PaymentEntity paymentEntity) {
        PaymentItemResponseModel paymentItemResponseModel = new PaymentItemResponseModel();
        paymentItemResponseModel.setId(paymentEntity.getId().toString());

        TicketSummaryEntity ticket = paymentEntity.getTicket();
        if (ticket != null) {
            paymentItemResponseModel.setFare(ticket.getFare());
            Constant.TicketType ticketType = Constant.TicketType.findByKey(ticket.getType());
            if (ticketType != null) {
                paymentItemResponseModel.setName(ticketType.getValue());
            }
        }

        paymentItemResponseModel.setActive(paymentEntity.isActive());

        String startDate = paymentEntity.getStartDate();
        if (!StringUtils.isEmpty(startDate)) {
            paymentItemResponseModel.setStartDate(DateTimeUtils.convertDateFormat(
                    startDate,
                    Constant.DateTimeFormat.YYYY_MM_DD,
                    Constant.DateTimeFormat.DD_MM_YYYY));
        }

        String endDate = paymentEntity.getEndDate();
        if (!StringUtils.isEmpty(endDate)) {
            paymentItemResponseModel.setEndDate(DateTimeUtils.convertDateFormat(
                    endDate,
                    Constant.DateTimeFormat.YYYY_MM_DD,
                    Constant.DateTimeFormat.DD_MM_YYYY));
        }

        ParkingAreaSummaryEntity parkingArea = paymentEntity.getParkingArea();
        if (parkingArea != null) {
            paymentItemResponseModel.setParkingAreaUser(parkingArea.getUsernameOwner());
            paymentItemResponseModel.setParkingAreaAddress(parkingArea.getAddress());
        }

        VehicleSummaryEntity vehicle = paymentEntity.getVehicle();
        if (vehicle != null) {
            paymentItemResponseModel.setVehicleUser(vehicle.getUsernameOwner());
            paymentItemResponseModel.setPlateNumber(vehicle.getPlateNumber());
        }

        return paymentItemResponseModel;
    }

    private String _prepareEndDate(TicketSummaryEntity ticketSummaryEntity, String startDateString) throws ParkingException {
        if (ticketSummaryEntity == null) {
            throw new ParkingException("Ticket is not null");
        }
        String endDate;
        Constant.TicketType ticketType = Constant.TicketType.findByKey(ticketSummaryEntity.getType());
        if (ticketType == null) {
            throw new ParkingException("Ticket type is not null");
        }
        LocalDate startDate;
        LocalDate localEndDate;
        switch (ticketType) {
            case MONTHLY:
                startDate = DateTimeUtils.convertStringToDate(startDateString, Constant.DateTimeFormat.DD_MM_YYYY);
                localEndDate = startDate.plusMonths(1);
                endDate = DateTimeUtils.convertDateFormat(localEndDate, Constant.DateTimeFormat.YYYY_MM_DD);
                break;
            case YEARLY:
                startDate = DateTimeUtils.convertStringToDate(startDateString, Constant.DateTimeFormat.DD_MM_YYYY);
                localEndDate = startDate.plusYears(1);
                endDate = DateTimeUtils.convertDateFormat(localEndDate, Constant.DateTimeFormat.YYYY_MM_DD);
                break;
            case ONE_TIME:
                endDate = DateTimeUtils.convertDateFormat(
                        startDateString,
                        Constant.DateTimeFormat.DD_MM_YYYY,
                        Constant.DateTimeFormat.YYYY_MM_DD);
                break;
            case FREE:
            default:
                endDate = "";
        }
        return endDate;
    }
}
