package com.huyendieu.parking.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("vehicles")
public class VehicleEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Field(name = "plate_number")
    private String plateNumber;

    @Field(name = "vehicle_Model")
    private String vehicleModel;

    @Field(name = "vehicle_brand")
    private String vehicleBrand;

    @Field(name = "register_date")
    private String registerDate;

    @Field(name = "owner")
    private UserEntity owner;

    @Field(name = "is_disable")
    private boolean disable;

    @Field(name = "is_active")
    private boolean active;
}
