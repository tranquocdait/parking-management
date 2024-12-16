package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRequestModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("parking_area_id")
    private String parkingAreaId;

    @JsonProperty("ticket_id")
    private String ticketId;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("prices")
    private float prices;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("status")
    private int status;
}
