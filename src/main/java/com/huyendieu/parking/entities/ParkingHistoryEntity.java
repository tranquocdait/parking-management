package com.huyendieu.parking.entities;

import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.entities.summary.PaymentSummaryEntity;
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
@Document("parking_histories")
public class ParkingHistoryEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Field(name = "check_in_date")
    private String checkInDate;

    @Field(name = "check_out_date")
    private String checkOutDate;

    @Field(name = "parking_area")
    private ParkingAreaSummaryEntity parkingArea;

    @Field(name = "vehicle")
    private VehicleSummaryEntity vehicle;

    @Field(name = "payment")
    private PaymentSummaryEntity payment;
}
