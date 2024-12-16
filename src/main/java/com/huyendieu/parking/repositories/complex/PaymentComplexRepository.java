package com.huyendieu.parking.repositories.complex;

import com.huyendieu.parking.entities.PaymentEntity;
import com.huyendieu.parking.model.dto.DashboardModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import org.bson.types.ObjectId;

import java.util.List;

public interface PaymentComplexRepository {

    List<PaymentEntity> findAllByPaging(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);

    List<PaymentEntity> findValidPaymentByUser(String username, String parkingAreaId, ObjectId ticketId);

    long countAll(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea);

    List<PaymentEntity> findAll(String userNameOwner, DashboardModel requestModel);
}
