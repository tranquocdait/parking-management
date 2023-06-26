package com.huyendieu.parking.entities;

import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
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
public class TicketEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Field(name = "fare")
    private float fare;

    @Field(name = "type")
    private int type;

    @Field(name = "parking_area")
    private ParkingAreaSummaryEntity parkingArea;

    @Field(name = "parent_id")
    private String parentId;

    @Field(name = "is_active")
    private boolean active;
}
