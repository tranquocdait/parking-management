package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdatePaymentRequestModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ticket_id")
    private String ticketId;

    @JsonProperty("start_date")
    private String startDate;
}
