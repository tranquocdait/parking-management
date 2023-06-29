package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import com.huyendieu.parking.model.response.PaymentListResponseModel;

public interface PaymentService {

    String create(PaymentRequestModel requestModel) throws ParkingException;

    void complete(String id) throws ParkingException;

    PaymentListResponseModel getPayments(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);
}
