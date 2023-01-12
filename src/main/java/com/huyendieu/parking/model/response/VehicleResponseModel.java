package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleResponseModel {

    @JsonProperty("vehicle_id")
    private String vehicleId;

    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("vehicle_model")
    private String vehicleModel;

    @JsonProperty("vehicle_brand")
    private String vehicleBrand;

    @JsonProperty("register_date")
    private String registerDate;

    @JsonProperty("username_owner")
    private String usernameOwner;

    @JsonProperty("register_parking_date")
    private String registerParkingDate;

    @JsonProperty("accepted_parking_date")
    private String acceptedParkingDate;

    @JsonProperty("status")
    private int status;

}
