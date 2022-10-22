package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.RoleUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleUserRepository extends MongoRepository<RoleUserEntity, String> {
    Optional<RoleUserEntity> findByCode(String Code);
}
