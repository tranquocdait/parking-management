package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ParkingRegistrationRequestModel {

    @JsonProperty("vehicle_ids")
    private List<String> vehicleIds;

    @JsonProperty("parking_area_id")
    private String parkingAreaId;

    @JsonProperty("status")
    private int status;
}
