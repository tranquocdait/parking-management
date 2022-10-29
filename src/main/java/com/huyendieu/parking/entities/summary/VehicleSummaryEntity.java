package com.huyendieu.parking.entities.summary;

import com.huyendieu.parking.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSummaryEntity {

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

    @Field(name = "username_owner")
    private String usernameOwner;

    @Field(name = "is_disable")
    private boolean disable;
}
