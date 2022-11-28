package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public class CheckParkingResponseModel {

    @JsonProperty("parking_id")
    private String parkingId;

    @JsonProperty("check_type")
    private String checkType;

    @JsonProperty("message")
    private String message;
}