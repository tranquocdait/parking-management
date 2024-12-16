package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInWithOutPerRequestModel {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("plate_number")
    private String plateNumber;
}
