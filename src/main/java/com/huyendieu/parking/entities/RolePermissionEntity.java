package com.huyendieu.parking.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@Document("role_permissions")
public class RolePermissionEntity extends BaseEntity {

    private boolean isAdmin;
}
