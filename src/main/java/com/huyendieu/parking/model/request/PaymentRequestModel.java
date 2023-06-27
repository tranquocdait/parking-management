package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRequestModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("parking_area_id")
    private String parkingAreaId;

    @JsonProperty("ticket_id")
    private String ticketId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("start_date")
    private String startDate;
}
