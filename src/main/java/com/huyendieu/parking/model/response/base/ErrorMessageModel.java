package com.huyendieu.parking.model.response.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageModel {

    @JsonProperty("message")
    private String message;
}
