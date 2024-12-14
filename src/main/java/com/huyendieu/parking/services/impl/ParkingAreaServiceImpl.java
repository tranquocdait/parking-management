package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.constants.NotificationConstant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.PaymentEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.dto.DashboardModel;
import com.huyendieu.parking.model.request.*;
import com.huyendieu.parking.model.response.*;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.PaymentRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.repositories.complex.ParkingHistoryComplexRepository;
import com.huyendieu.parking.repositories.complex.PaymentComplexRepository;
import com.huyendieu.parking.repositories.complex.VehicleComplexRepository;
import com.huyendieu.parking.services.ParkingAreaService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.utils.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingAreaServiceImpl extends BaseService implements ParkingAreaService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingHistoryComplexRepository parkingHistoryComplexRepository;

    @Autowired
    private VehicleComplexRepository vehicleComplexRepository;

    @Autowired
    private PaymentComplexRepository paymentComplexRepository;

    @Override
    public QRCodeResponseModel generateQR(String username, boolean isViewAll) throws ParkingException {

        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            throw new ParkingException("authentication don't exist!");
        }
        String parkingAreaId = parkingAreaEntity.getId().toString();
        if (isViewAll) {
            NotificationUtils.removeNotification((String.format(NotificationConstant.NotificationPath.CHECKING, parkingAreaId)));
        }
        return QRCodeResponseModel.builder()
                .userId(parkingAreaId)
                .qrCode(QRCodeUtils.generateQRCodeImage(parkingAreaId))
                .build();
    }

    @Override
    public TrackingParkingAreaResponseModel trackingManage(Authentication authentication, TrackingParkingRequestModel trackingParkingRequestModel)
            throws ParkingException {
        String username = UserUtils.getUserName(authentication);

        List<TrackingParkingAreaItemResponseModel> trackingParkingAreaModels = new ArrayList<>();
        List<ParkingHistoryEntity> parkingHistoryEntities = parkingHistoryComplexRepository.findAllByPaging(username, trackingParkingRequestModel);
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

    @Override
    public VehicleManagementResponseModel vehicleManage(Authentication authentication,
                                                        TrackingParkingRequestModel trackingParkingRequestModel) throws ParkingException {
        VehicleManagementResponseModel responseModel = new VehicleManagementResponseModel();
        String username = UserUtils.getUserName(authentication);
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            return responseModel;
        }
        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();
        List<VehicleResponseModel> vehicleResponseModels = new ArrayList<>();
        responseModel.setDataList(vehicleResponseModels);
        if (CollectionUtils.isEmpty(vehicles)) {
            return responseModel;
        }

        for (VehicleSummaryEntity vehicle : vehicles) {
            vehicleResponseModels.add(mappingVehicle(vehicle));
        }

        responseModel.setTotalRecord(vehicleResponseModels.size());
        return responseModel;
    }

    @Override
    public DashboardResponseModel checkingStatistics(Authentication authentication, DashboardRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        int dashboardType = requestModel.getType();

        DashboardResponseModel responseModel = new DashboardResponseModel();

        // label
        List<String> labels = mappingLabel(dashboardType);
        responseModel.setLabel(labels);

        // data
        DashboardModel dashboardModel = getDashboardModel(dashboardType);
        List<ParkingHistoryEntity> parkingHistoryEntities = parkingHistoryComplexRepository.findAll(username, dashboardModel);
        if (CollectionUtils.isEmpty(parkingHistoryEntities)) {
            return responseModel;
        }
        Map<String, Float> mapData = initDashboardData(labels);
        for (ParkingHistoryEntity parkingHistoryEntity : parkingHistoryEntities) {
            String day = DateTimeUtils.convertDateTimeFormat(
                    parkingHistoryEntity.getCheckOutDate(),
                    Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                    Constant.DateTimeFormat.DD_MM
            );
            mapData.put(day, mapData.get(day) + 1);
        }
        responseModel.setTotal(parkingHistoryEntities.size());
        responseModel.setData(new ArrayList<>(mapData.values()));
        return responseModel;
    }

    @Override
    public DashboardResponseModel incomeStatistics(Authentication authentication, DashboardRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        int dashboardType = requestModel.getType();

        DashboardResponseModel responseModel = new DashboardResponseModel();

        // label
        List<String> labels = mappingLabel(dashboardType);
        responseModel.setLabel(labels);

        // data
        DashboardModel dashboardModel = getDashboardModel(dashboardType);
        List<PaymentEntity> paymentEntities = paymentComplexRepository.findAll(username, dashboardModel);
        if (CollectionUtils.isEmpty(paymentEntities)) {
            return responseModel;
        }
        Map<String, Float> mapData = initDashboardData(labels);
        float total = 0;
        for (PaymentEntity paymentEntity : paymentEntities) {
            String day = DateTimeUtils.convertDateTimeFormat(
                    paymentEntity.getStartDate(),
                    Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                    Constant.DateTimeFormat.DD_MM
            );
            float prices = paymentEntity.getPrices();
            mapData.put(day, mapData.get(day) + prices);
            total += prices;
        }
        responseModel.setTotal(total);
        responseModel.setData(new ArrayList<>(mapData.values()));
        return responseModel;
    }

    @Override
    public int parkingRegistration(Authentication authentication, ParkingRegistrationRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            throw new ParkingException("authentication don't exist!");
        }

        validateRequest(requestModel);
        List<String> vehicleIds = requestModel.getVehicleIds();
        int status = requestModel.getStatus();
        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();
        if (CollectionUtils.isEmpty(vehicles)) {
            // the first register
            vehicles = new ArrayList<>();
            parkingAreaEntity.setVehicles(vehicles);
        }

        for (String vehicleId : vehicleIds) {
            VehicleSummaryEntity vehicleSummaryEntity = null;
            if (!CollectionUtils.isEmpty(vehicles)) {
                for (VehicleSummaryEntity vehicle : vehicles) {
                    String vehicleEntityId = vehicle.getId().toString();
                    if (vehicleId.equals(vehicleEntityId)) {
                        vehicleSummaryEntity = vehicle;
                    }
                }
            }
            if (vehicleSummaryEntity == null) {
                Optional<VehicleEntity> vehicleEntityOptional = vehicleRepository.findFirstById(new ObjectId(vehicleId));
                if (!vehicleEntityOptional.isPresent()) {
                    return 0;
                }
                VehicleEntity vehicleEntity = vehicleEntityOptional.get();
                vehicleSummaryEntity = mappingVehicleSummary(vehicleEntity, status);
                vehicles.add(vehicleSummaryEntity);
            } else {
                if (status == Constant.ParkingRegistrationStatus.ACCEPT.getKey()) {
                    vehicleSummaryEntity.setAcceptedParkingDate(currentDate());
                }
                vehicleSummaryEntity.setStatus(status);
            }
        }

        parkingAreaRepository.save(parkingAreaEntity);
        return status;
    }

    @Override
    public VehicleListResponseModel getVehicles(Authentication authentication, VehicleRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();
        List<ObjectId> excludedIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vehicles)) {
            excludedIds = vehicles.stream().map(ele -> ele.getId()).collect(Collectors.toList());
        }
        List<String> excludedVehicleIds = requestModel.getExcludedVehicleIds();
        if (!CollectionUtils.isEmpty(excludedVehicleIds)) {
            excludedIds.addAll(excludedVehicleIds.stream().map(ele -> new ObjectId(ele)).collect(Collectors.toList()));
        }

        List<VehiclePopupResponseModel> vehiclePopupResponseModels = new ArrayList<>();
        List<VehicleEntity> vehicleEntities = vehicleComplexRepository.findAllByPaging(requestModel, excludedIds);
        long totalRecord = vehicleComplexRepository.countAll(requestModel, excludedIds);

        if (!CollectionUtils.isEmpty(vehicleEntities)) {
            for (VehicleEntity vehicleEntity : vehicleEntities) {
                vehiclePopupResponseModels.add(mappingVehicleData(vehicleEntity));
            }
        }
        return VehicleListResponseModel.builder()
                .dataList(vehiclePopupResponseModels)
                .totalRecord(totalRecord)
                .build();
    }

    @Override
    public CapacityResponseModel getCapacityInformation(Authentication authentication) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        return getCapacityByParkingArea(parkingAreaEntity);
    }

    @Override
    public CapacityResponseModel getCapacityByParkingArea(ParkingAreaEntity parkingAreaEntity) {
        if (parkingAreaEntity == null) {
            return CapacityResponseModel.builder().build();
        }
        TrackingParkingRequestModel trackingParkingRequestModel = new TrackingParkingRequestModel();
        trackingParkingRequestModel.setType(Constant.CheckParkingCode.CHECK_IN.getKey());

        String username = parkingAreaEntity.getOwner().getUserName();
        long occupation = parkingHistoryComplexRepository.countAll(username, trackingParkingRequestModel);

        return CapacityResponseModel.builder()
                .id(parkingAreaEntity.getId().toString())
                .capacity(parkingAreaEntity.getCapacity())
                .occupation((int) occupation)
                .build();
    }

    @Override
    public int unsubscribeParkingArea(Authentication authentication, UnsubscribeParkingRequestModel requestModel) throws ParkingException {
        String username = UserUtils.getUserName(authentication);
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwner(username);
        if (parkingAreaEntity == null) {
            throw new ParkingException("authentication don't exist!");
        }
        String vehicleId = requestModel.getVehicleId();
        if (StringUtils.isEmpty(vehicleId)) {
            throw new ParkingException("vehicleId is incorrect!");
        }

        List<VehicleSummaryEntity> vehicles = parkingAreaEntity.getVehicles();
        if (CollectionUtils.isEmpty(vehicles)) {
            return 0;
        }

        VehicleSummaryEntity removedVehicle = null;
        for (VehicleSummaryEntity vehicle : vehicles) {
            if (vehicleId.equals(vehicle.getId().toString())) {
                removedVehicle = vehicle;
                break;
            }
        }

        if (removedVehicle == null) {
            return 0;
        }

        vehicles.remove(removedVehicle);
        parkingAreaRepository.save(parkingAreaEntity);

        return 1;
    }

    private void validateRequest(ParkingRegistrationRequestModel requestModel) throws ParkingException {
        List<String> vehicleIds = requestModel.getVehicleIds();
        int status = requestModel.getStatus();
        List<String> messageErrors = new ArrayList<>();
        if (CollectionUtils.isEmpty(vehicleIds)) {
            messageErrors.add("vehicleId isn't blank or null!");
        }
        Constant.ParkingRegistrationStatus registrationStatus = Constant.ParkingRegistrationStatus.findByKey(status);
        if (registrationStatus == null) {
            messageErrors.add("vehicleId is incorrect!");
        }
        if (!CollectionUtils.isEmpty(messageErrors)) {
            throw new ParkingException(String.join(", ", messageErrors));
        }
    }

    private VehicleSummaryEntity mappingVehicleSummary(VehicleEntity vehicleEntity, int status) {
        if (vehicleEntity == null) {
            return null;
        }
        VehicleSummaryEntity vehicleSummary = MapperUtils.map(vehicleEntity, VehicleSummaryEntity.class);
        vehicleSummary.setUsernameOwner(vehicleEntity.getOwner() != null
                ? vehicleEntity.getOwner().getUserName() : Constant.Character.BLANK);
        vehicleSummary.setRegisterParkingDate(currentDate());
        if (status == Constant.ParkingRegistrationStatus.ACCEPT.getKey()) {
            vehicleSummary.setAcceptedParkingDate(currentDate());
        }
        vehicleSummary.setStatus(status);
        return vehicleSummary;
    }

    private Map<String, Float> initDashboardData(List<String> labels) {
        Map<String, Float> mapData = new TreeMap<>();
        if (CollectionUtils.isEmpty(labels)) {
            return mapData;
        }
        for (String label : labels) {
            mapData.put(label, 0F);
        }
        return mapData;
    }

    private List<String> mappingLabel(int dashboardType) {
        LocalDate todayDate = LocalDate.now();
        int month = todayDate.getMonthValue();
        String monthStr = String.valueOf(month);
        if (month < 10) {
            monthStr = 0 + String.valueOf(month);
        }
        List<String> labels = new ArrayList<>();
        if (dashboardType == Constant.DashboardType.MONTH) {
            for (int day = 1; day <= todayDate.getMonth().length(todayDate.isLeapYear()); day++) {
                String dayStr = String.valueOf(day);
                if (day < 10) {
                    dayStr = 0 + String.valueOf(day);
                }
                labels.add(dayStr + Constant.Character.VIRGULE + monthStr);
            }
        }
        return labels;
    }

    private DashboardModel getDashboardModel(int dashboardType) {
        DashboardModel dashboardModel = new DashboardModel();
        LocalDate todayDate = LocalDate.now();
        if (dashboardType == Constant.DashboardType.MONTH) {
            // date from
            LocalDate firstDate = todayDate.withDayOfMonth(1);
            dashboardModel.setDateFrom(DateTimeUtils.convertDateFormat(firstDate,
                    Constant.DateTimeFormat.YYYY_MM_DD)
                    + Constant.Character.SPACE
                    + Constant.DateTimeFormat.FIRST_TIME_OF_DATE);

            // date to
            LocalDate lastDate = todayDate.withDayOfMonth(todayDate.getMonth().length(todayDate.isLeapYear()));
            dashboardModel.setDateTo(DateTimeUtils.convertDateFormat(lastDate,
                    Constant.DateTimeFormat.YYYY_MM_DD)
                    + Constant.Character.SPACE
                    + Constant.DateTimeFormat.LAST_TIME_OF_DATE);
        }
        return dashboardModel;
    }

    private VehicleResponseModel mappingVehicle(VehicleSummaryEntity vehicleEntity) {
        if (vehicleEntity == null) {
            return null;
        }
        VehicleResponseModel vehicleResponseModel = MapperUtils.map(vehicleEntity, VehicleResponseModel.class);
        vehicleResponseModel.setVehicleId(vehicleEntity.getId().toString());
        vehicleResponseModel.setRegisterDate(DateTimeUtils.convertDateFormat(
                vehicleEntity.getRegisterDate(),
                Constant.DateTimeFormat.YYYY_MM_DD,
                Constant.DateTimeFormat.DD_MM_YYYY));
        vehicleResponseModel.setRegisterParkingDate(DateTimeUtils.convertDateTimeFormat(
                vehicleEntity.getRegisterParkingDate(),
                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS));
        vehicleResponseModel.setAcceptedParkingDate(DateTimeUtils.convertDateTimeFormat(
                vehicleEntity.getAcceptedParkingDate(),
                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS));
        vehicleResponseModel.setStatus(vehicleEntity.getStatus());

        return vehicleResponseModel;
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
            model.setVehicleModel(vehicleSummaryEntity.getVehicleModel());
            model.setVehicleBrand(vehicleSummaryEntity.getVehicleBrand());
            model.setPlateNumber(vehicleSummaryEntity.getPlateNumber());
            model.setVehicleOwner(vehicleSummaryEntity.getUsernameOwner());
        }
        return model;
    }

    private VehiclePopupResponseModel mappingVehicleData(VehicleEntity entity) {
        VehiclePopupResponseModel model = new VehiclePopupResponseModel();
        model.setVehicleId(entity.getId().toString());
        model.setPlateNumber(entity.getPlateNumber());
        model.setUsernameOwner(entity.getOwner() != null ? entity.getOwner().getUserName() : "");
        return model;
    }
}
