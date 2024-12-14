package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentItemResponseModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fare")
    private float fare;

    @JsonProperty("type")
    private int type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("prices")
    private float prices;

    @JsonProperty("parking_area_user")
    private String parkingAreaUser;

    @JsonProperty("parking_area_address")
    private String parkingAreaAddress;

    @JsonProperty("vehicle_user")
    private String vehicleUser;

    @JsonProperty("plate_number")
    private String plateNumber;
}
