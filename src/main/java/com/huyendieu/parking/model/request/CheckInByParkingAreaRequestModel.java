package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInByParkingAreaRequestModel {

    @JsonProperty("shorted_plate_number")
    private String shortedPlateNumber;

    @JsonProperty("image")
    private String imageBase64;
}
