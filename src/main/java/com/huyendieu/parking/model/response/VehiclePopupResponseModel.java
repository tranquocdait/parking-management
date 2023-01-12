package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehiclePopupResponseModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("username_owner")
    private String usernameOwner;
}
