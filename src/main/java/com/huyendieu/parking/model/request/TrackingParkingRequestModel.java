package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrackingParkingRequestModel {

    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("keyword")
    private String keyword;
    
    @JsonProperty("type")
    private int type;
}
