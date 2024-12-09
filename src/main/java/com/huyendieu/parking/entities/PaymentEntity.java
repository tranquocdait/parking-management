package com.huyendieu.parking.entities;

import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.TicketSummaryEntity;
import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
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
@Document("payments")
public class PaymentEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Field(name = "vehicle")
    private VehicleSummaryEntity vehicle;

    @Field(name = "ticket")
    private TicketSummaryEntity ticket;

    @Field(name = "parking_area")
    private ParkingAreaSummaryEntity parkingArea;

    @Field(name = "is_active")
    private boolean active;

    @Field(name = "status")
    private int status;

    @Field(name = "prices")
    private float prices;

    @Field(name = "start_date")
    private String startDate;

    @Field(name = "end_date")
    private String endDate;
}
