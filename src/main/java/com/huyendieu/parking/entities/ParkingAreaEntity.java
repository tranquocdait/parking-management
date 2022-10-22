package com.huyendieu.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("parking_areas")
public class ParkingAreaEntity extends BaseEntity {

    @Id
    private ObjectId userID;

    @Field(name = "address")
    private String address;

    @Field(name = "province")
    private String province;

    @Field(name = "district")
    private String district;

    @Field(name = "commune")
    private String commune;

    @Field(name = "owner")
    private UserEntity owner;

    @Field(name = "vehicles")
    private List<VehicleEntity> vehicles;

    @Field(name = "is_disable")
    private boolean disable;
}
