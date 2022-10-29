package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.ParkingAreaEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ParkingAreaRepository extends MongoRepository<ParkingAreaEntity, String> {

    Optional<ParkingAreaEntity> findFistById(ObjectId id);

    @Query("{'owner.id' : ?0, 'disable': false }")
    ParkingAreaEntity findFirstByOwnerId(Object userId);

    @Query("{'owner.user_name' : ?0, 'disable': false }")
    ParkingAreaEntity findFirstByOwner(String username);
}
