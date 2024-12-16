package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.dto.PaymentInfoModel;
import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import com.huyendieu.parking.model.request.UpdatePaymentRequestModel;
import com.huyendieu.parking.model.response.CheckParkingResponseModel;
import com.huyendieu.parking.model.response.PaymentListResponseModel;

public interface PaymentService {

    String create(PaymentRequestModel requestModel) throws ParkingException;

    String update(UpdatePaymentRequestModel requestModel) throws ParkingException;

    String delete(String id) throws ParkingException;

    void complete(String paymentId) throws ParkingException;

    PaymentListResponseModel getPayments(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);

    PaymentInfoModel isPassCheckout(String username, String parkingAreaId);
}
