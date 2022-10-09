package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.RoleUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleUserRepository extends MongoRepository<RoleUserEntity, String> {
}
