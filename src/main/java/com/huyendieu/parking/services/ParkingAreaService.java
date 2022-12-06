package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.response.QRCodeResponseModel;
import com.huyendieu.parking.model.response.TrackingParkingAreaResponseModel;
import org.springframework.security.core.Authentication;

public interface ParkingAreaService {

    QRCodeResponseModel generateQR(String username, boolean isViewAll) throws ParkingException;

    TrackingParkingAreaResponseModel trackingManage(Authentication authentication,
                                                    TrackingParkingRequestModel trackingParkingRequestModel) throws ParkingException;
}
	