package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUserNameIgnoreCase(String userName);

    Optional<UserEntity> findByUserNameAndDisableIsFalse(String userName);
}
