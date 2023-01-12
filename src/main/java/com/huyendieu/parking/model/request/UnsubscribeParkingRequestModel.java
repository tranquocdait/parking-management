package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UnsubscribeParkingRequestModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;
}
