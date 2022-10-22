package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseModel {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("role_code")
    private String roleCode;

    @JsonProperty("vehicles")
    List<VehicleResponseModel> vehicles;

    @JsonProperty("parking_area")
    ParkingAreaResponseModel parkingArea;
}
