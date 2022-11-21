package com.huyendieu.parking.entities;

import javax.validation.constraints.Size;

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
@Document("role_users")
public class RoleUserEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Size(min = 2)
    @Field(name = "code")
    private String code;

    @Size(min = 2)
    @Field(name = "name")
    private String name;

}
