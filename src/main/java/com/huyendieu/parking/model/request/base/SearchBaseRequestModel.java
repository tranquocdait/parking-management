package com.huyendieu.parking.model.request.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchBaseRequestModel {
    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("excluded_vehicle_ids")
    private List<String> excludedVehicleIds;
}
