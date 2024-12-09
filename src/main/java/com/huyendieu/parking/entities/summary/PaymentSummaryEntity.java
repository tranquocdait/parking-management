package com.huyendieu.parking.entities.summary;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class PaymentSummaryEntity {
    @Id
    private ObjectId id;

    @Field(name = "prices")
    private float prices;

    @Field(name = "status")
    private int status;
}
