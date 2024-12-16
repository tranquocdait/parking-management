package com.huyendieu.parking.services;

import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.*;
import com.huyendieu.parking.model.response.*;
import org.springframework.security.core.Authentication;

public interface ParkingAreaService {

    QRCodeResponseModel generateQR(String username, boolean isViewAll) throws ParkingException;

    TrackingParkingAreaResponseModel trackingManage(Authentication authentication,
                                                    TrackingParkingRequestModel trackingParkingRequestModel) throws ParkingException;

    VehicleManagementResponseModel vehicleManage(Authentication authentication,
                                                 TrackingParkingRequestModel trackingParkingRequestModel) throws ParkingException;

    DashboardResponseModel checkingStatistics(Authentication authentication,
                                              DashboardRequestModel requestModel) throws ParkingException;

    int parkingRegistration(Authentication authentication, ParkingRegistrationRequestModel requestModel) throws ParkingException;

    VehicleListResponseModel getVehicles(Authentication authentication, VehicleRequestModel requestModel) throws ParkingException;

    int unsubscribeParkingArea(Authentication authentication, UnsubscribeParkingRequestModel requestModel) throws ParkingException;

    CapacityResponseModel getCapacityInformation(Authentication authentication) throws ParkingException;

    CapacityResponseModel getCapacityByParkingArea(ParkingAreaEntity parkingAreaEntity);

    DashboardResponseModel incomeStatistics(Authentication authentication, DashboardRequestModel requestModel) throws ParkingException;
}
	