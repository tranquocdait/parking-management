package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.PaymentRequestModel;

public interface PaymentService {

    void create(PaymentRequestModel requestModel) throws ParkingException;

}
