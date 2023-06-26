package com.huyendieu.parking.model.response.common.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComboboxResponseModel {

    private Integer key;

    private String code;

    private String value;
}
