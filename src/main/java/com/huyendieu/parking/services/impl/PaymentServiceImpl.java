package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.PaymentRequestModel;
import com.huyendieu.parking.services.PaymentService;
import com.huyendieu.parking.services.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

    @Autowired
    private MessageSource messageSource;


    @Override
    public void create(PaymentRequestModel requestModel) throws ParkingException {

    }
}
