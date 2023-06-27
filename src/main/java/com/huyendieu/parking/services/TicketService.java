package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.SearchTicketRequestModel;
import com.huyendieu.parking.model.request.TicketRequestModel;
import com.huyendieu.parking.model.response.TicketListResponseModel;
import com.huyendieu.parking.model.response.common.base.ListComboboxResponseModel;

public interface TicketService {

    void create(String userName, TicketRequestModel requestModel) throws ParkingException;

    void update(String userName, TicketRequestModel requestModel) throws ParkingException;

    ListComboboxResponseModel getTypes();

    TicketListResponseModel getTickets(String userName, SearchTicketRequestModel requestModel);
}
