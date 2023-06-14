package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huyendieu.parking.model.request.base.SearchBaseRequestModel;
import lombok.Data;

import java.util.List;

@Data
public class VehicleRequestModel extends SearchBaseRequestModel {

    @JsonProperty("excluded_vehicle_ids")
    private List<String> excludedVehicleIds;
}
