package com.huyendieu.parking.entities.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingAreaSummaryEntity {

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

    @Field(name = "username_owner")
    private String usernameOwner;

    @Field(name = "is_disable")
    private boolean disable;
}
