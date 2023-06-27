package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketItemResponseModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fare")
    private float fare;

    @JsonProperty("type")
    private int type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("created_date")
    private String createdDate;
}
