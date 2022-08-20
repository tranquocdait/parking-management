package com.huyendieu.parking.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("test-collection")
@Data
public class TestEntity {

    @Id
    private ObjectId id;

    @Field("user_name")
    private String userName;
}
