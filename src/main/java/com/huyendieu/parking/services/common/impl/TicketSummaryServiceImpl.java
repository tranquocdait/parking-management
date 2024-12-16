package com.huyendieu.parking.services.common.impl;

import com.huyendieu.parking.entities.TicketEntity;
import com.huyendieu.parking.entities.summary.TicketSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.repositories.TicketRepository;
import com.huyendieu.parking.services.common.TicketSummaryService;
import com.huyendieu.parking.utils.MapperUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketSummaryServiceImpl implements TicketSummaryService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public TicketSummaryEntity mappingSummaryById(String ticketId) throws ParkingException {
        Optional<TicketEntity> optionalTicket = ticketRepository.findFirstById(new ObjectId(ticketId));
        if (optionalTicket.isEmpty()) {
            return null;
        }
        TicketEntity vehicleEntity = optionalTicket.get();
        return MapperUtils.map(vehicleEntity, TicketSummaryEntity.class);
    }
}
