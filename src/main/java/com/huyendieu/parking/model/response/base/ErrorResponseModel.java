package com.huyendieu.parking.model.response.base;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseModel extends BaseResponseModel {

    public ErrorResponseModel() {
        super(HttpStatus.BAD_REQUEST.value(), null);
    }

    public ErrorResponseModel(Object message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
