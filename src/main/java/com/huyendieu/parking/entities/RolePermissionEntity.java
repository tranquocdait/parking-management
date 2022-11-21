 package com.huyendieu.parking.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Document("role_permissions")
public class RolePermissionEntity extends BaseEntity {

    private boolean isAdmin;
}
