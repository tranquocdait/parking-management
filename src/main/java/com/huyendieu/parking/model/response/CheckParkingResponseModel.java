package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
public class CheckParkingResponseModel {

    @JsonProperty("parking_id")
    private String parkingId;

    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("check_type")
    private String checkType;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("prices")
    private float prices;

    @JsonProperty("check_in_date")
    private String checkInDate;

    @JsonProperty("check_out_date")
    private String checkOutDate;

    @JsonProperty("message")
    private String message;
}