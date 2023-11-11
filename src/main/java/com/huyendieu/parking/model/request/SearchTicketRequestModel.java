package com.huyendieu.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huyendieu.parking.model.request.base.SearchBaseRequestModel;
import lombok.Data;

@Data
public class SearchTicketRequestModel extends SearchBaseRequestModel {

    @JsonProperty("type")
    private int type;
}
