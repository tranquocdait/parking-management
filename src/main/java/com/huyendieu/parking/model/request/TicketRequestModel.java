package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketRequestModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fare")
    private float fare;

    @JsonProperty("type")
    private int type;
}
