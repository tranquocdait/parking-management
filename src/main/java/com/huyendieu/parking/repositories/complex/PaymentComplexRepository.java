package com.huyendieu.parking.repositories.complex;

import com.huyendieu.parking.entities.PaymentEntity;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;

import java.util.List;

public interface PaymentComplexRepository {

    List<PaymentEntity> findAllByPaging(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);

    long countAll(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);
}
