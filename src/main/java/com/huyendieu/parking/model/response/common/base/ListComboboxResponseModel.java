package com.huyendieu.parking.model.response.common.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class ListComboboxResponseModel {

    @JsonProperty("data_list")
    private List<ComboboxResponseModel> dataList;
}
