package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DashboardRequestModel {

    @JsonProperty("type")
    private int type;
}
