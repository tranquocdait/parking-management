package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackingVehicleItemResponseModel {

    @JsonProperty("check_in_date")
    private String checkInDate;

    @JsonProperty("check_out_date")
    private String checkOutDate;

    @JsonProperty("parking_id")
    private String parkingId;

    @JsonProperty("packing_owner")
    private String packingOwner;
}
