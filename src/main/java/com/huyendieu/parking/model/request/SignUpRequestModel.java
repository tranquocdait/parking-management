package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignUpRequestModel {

    @JsonProperty("user_name")
    @NotBlank(message = "user_name may not be blank")
    private String userName;

    @JsonProperty("password")
    @NotBlank(message = "password may not be blank")
    private String password;

    @JsonProperty("first_name")
    @NotBlank(message = "first_name may not be blank")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "last_name may not be blank")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone_number may not be blank")
    private String phoneNumber;

    @JsonProperty("is_vehicle_owner")
    private boolean vehicleOwner;

    // vehicle owner info
    @JsonProperty("plate_number")
    @NotBlank(message = "plate_number may not be blank")
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
    @NotBlank(message = "province may not be blank")
    private String province;

    @JsonProperty("district")
    @NotBlank(message = "district may not be blank")
    private String district;

    @JsonProperty("commune")
    @NotBlank(message = "commune may not be blank")
    private String commune;

    @JsonProperty("capacity")
    private int capacity;
}
