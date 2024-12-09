package com.huyendieu.parking.model.dto;

import com.huyendieu.parking.entities.TicketEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfoModel {

    private TicketEntity ticket;

    private boolean isPass;
}
