package com.huyendieu.parking.entities.custom;

import org.springframework.data.mongodb.core.mapping.Field;

public class TotalEntity {

    @Field(name = "count")
    private long total;
}
