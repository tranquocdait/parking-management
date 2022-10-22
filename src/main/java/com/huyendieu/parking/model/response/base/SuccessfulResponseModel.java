package com.huyendieu.parking.model.response.base;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SuccessfulResponseModel extends BaseResponseModel {

    public SuccessfulResponseModel() {
        super(HttpStatus.OK.value(), null);
    }

    public SuccessfulResponseModel(Object data) {
        super(HttpStatus.OK.value(), data);
    }
}
