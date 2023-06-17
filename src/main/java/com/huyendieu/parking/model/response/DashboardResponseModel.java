package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DashboardResponseModel {

    @JsonProperty("label")
    private List<String> label;

    @JsonProperty("total")
    private float total;

    @JsonProperty("data")
    private List<Float> data;
}
