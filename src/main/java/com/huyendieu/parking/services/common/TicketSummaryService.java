package com.huyendieu.parking.services.common;

import com.huyendieu.parking.entities.summary.TicketSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;

public interface TicketSummaryService {
    TicketSummaryEntity mappingSummaryById(String ticketId) throws ParkingException;
}
