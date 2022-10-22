package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.ParkingAreaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ParkingAreaRepository extends MongoRepository<ParkingAreaEntity, String> {

    @Query("{'owner.id' : ?0, 'disable': false }")
    ParkingAreaEntity findFirstByOwnerId(Object userId);
}
