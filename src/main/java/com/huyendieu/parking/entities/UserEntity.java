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
@Document("users")
public class UserEntity extends BaseEntity {

    @Id
    private ObjectId id;

    @Size(min = 2)
    @Field(name = "user_name")
    private String userName;

    @Size(min = 8)
    @Field(name = "password")
    private String password;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "email")
    private String email;

    @Field(name = "phone_number")
    private String phoneNumber;

    @Field(name = "is_disable")
    private boolean disable;

    @Field(name = "role_user")
    private RoleUserEntity roleUser;

}
