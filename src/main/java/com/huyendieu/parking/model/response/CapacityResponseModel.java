package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CapacityResponseModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("occupation")
    private int occupation;
}
