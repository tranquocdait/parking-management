package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackingParkingAreaItemResponseModel {

    @JsonProperty("check_in_date")
    private String checkInDate;

    @JsonProperty("check_out_date")
    private String checkOutDate;

    @JsonProperty("plate_number")
    private String plateNumber;

    @JsonProperty("vehicle_model")
    private String vehicleModel;

    @JsonProperty("vehicle_brand")
    private String vehicleBrand;

    @JsonProperty("vehicle_owner")
    private String vehicleOwner;

    @JsonProperty("check_in_url")
    private String checkInUrl;

    @JsonProperty("check_out_url")
    private String checkOutUrl;
}
