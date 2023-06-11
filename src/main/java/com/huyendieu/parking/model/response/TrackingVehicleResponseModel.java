package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackingVehicleResponseModel {

    @JsonProperty("data_list")
    private List<TrackingVehicleItemResponseModel> dataList;

    @JsonProperty("total_record")
    private long totalRecord;
}
