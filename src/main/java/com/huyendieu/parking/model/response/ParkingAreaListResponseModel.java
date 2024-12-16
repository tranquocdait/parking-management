package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParkingAreaListResponseModel {

    @JsonProperty("data_list")
    private List<ParkingAreaResponseModel> dataList;

    @JsonProperty("total_record")
    private long totalRecord;
}
