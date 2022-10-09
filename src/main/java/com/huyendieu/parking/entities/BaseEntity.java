package com.huyendieu.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_date")
    private String createdDate;

    @Field(name = "updated_by")
    private String updatedBy;

    @Field(name = "updated_date")
    private String updatedDate;
}
