package com.huyendieu.parking.entities.summary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("tickets")
public class TicketSummaryEntity {

    @Id
    private ObjectId id;

    @Field(name = "fare")
    private float fare;

    @Field(name = "type")
    private int type;

    @Field(name = "parking_area")
    private ParkingAreaSummaryEntity parkingArea;
}
