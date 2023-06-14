package com.huyendieu.parking.model.request.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchBaseRequestModel {
    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("keyword")
    private String keyword;
}
