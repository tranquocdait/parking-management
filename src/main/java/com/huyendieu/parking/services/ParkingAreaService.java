package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.DashboardRequestModel;
import com.huyendieu.parking.model.request.ParkingRegistrationRequestModel;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.DashboardResponseModel;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import com.huyendieu.parking.model.response.VehicleManagementResponseModel;
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
}
	