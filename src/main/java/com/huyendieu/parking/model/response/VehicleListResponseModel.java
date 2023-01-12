package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VehicleListResponseModel {

    @JsonProperty("data_list")
    private List<VehiclePopupResponseModel> dataList;

    @JsonProperty("total_record")
    private long totalRecord;
}
