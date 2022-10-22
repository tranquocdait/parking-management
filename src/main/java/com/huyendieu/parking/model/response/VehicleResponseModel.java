package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleResponseModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("vehicle_Model")
    private String vehicleModel;

    @JsonProperty("vehicle_brand")
    private String vehicleBrand;

    @JsonProperty("register_date")
    private String registerDate;

}
