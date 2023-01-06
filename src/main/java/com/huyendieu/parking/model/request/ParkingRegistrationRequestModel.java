package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParkingRegistrationRequestModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("parking_area_id")
    private String parkingAreaId;

    @JsonProperty("status")
    private int status;
}
