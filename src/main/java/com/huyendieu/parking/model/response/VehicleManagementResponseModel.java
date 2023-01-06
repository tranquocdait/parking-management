package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleManagementResponseModel {

    @JsonProperty("data_list")
    private List<VehicleResponseModel> dataList;

    @JsonProperty("total_record")
    private long totalRecord;
}
