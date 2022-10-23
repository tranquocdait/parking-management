package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignUpRequestModel {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("is_vehicle_owner")
    private boolean vehicleOwner;

    // vehicle owner info
    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("vehicle_model")
    private String vehicleModel;

    @JsonProperty("vehicle_brand")
    private String vehicleBrand;

    @JsonProperty("register_date")
    private String registerDate;

    // parking info
    @JsonProperty("address")
    private String address;

    @JsonProperty("province")
    private String province;

    @JsonProperty("district")
    private String district;

    @JsonProperty("commune")
    private String commune;
}
