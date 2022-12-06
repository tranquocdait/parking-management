package com.huyendieu.parking.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QRCodeResponseModel {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("qr_code")
    private String qrCode;
}
