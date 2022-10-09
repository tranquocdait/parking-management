package com.huyendieu.parking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;
}
