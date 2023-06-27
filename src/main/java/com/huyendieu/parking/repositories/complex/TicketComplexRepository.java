package com.huyendieu.parking.repositories.complex;

import com.huyendieu.parking.entities.TicketEntity;
import com.huyendieu.parking.model.request.SearchTicketRequestModel;

import java.util.List;

public interface TicketComplexRepository {

    List<TicketEntity> findAllByPaging(SearchTicketRequestModel requestModel, String userName);

    long countAll(SearchTicketRequestModel requestModel, String userName);
}
