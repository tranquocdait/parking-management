package com.huyendieu.parking.entities;

import com.huyendieu.parking.entities.summary.VehicleSummaryEntity;
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
    private ObjectId id;

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
    private List<VehicleSummaryEntity> vehicles;

    @Field(name = "is_disable")
    private boolean disable;

    @Field(name = "capacity")
    private int capacity;
}
