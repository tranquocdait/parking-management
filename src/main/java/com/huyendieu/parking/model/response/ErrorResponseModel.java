package com.huyendieu.parking.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseModel extends BaseResponseModel {

    public ErrorResponseModel() {
        super(HttpStatus.OK.value(), null);
    }

    public ErrorResponseModel(Object data) {
        super(HttpStatus.BAD_REQUEST.value(), data);
    }
}
