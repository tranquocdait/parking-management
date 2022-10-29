package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.VehicleEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VehicleRepository extends MongoRepository<VehicleEntity, String> {

    @Query("{ 'owner.id' : ?0, 'disable': false }")
    List<VehicleEntity> findAllByOwnerId(ObjectId id);

    @Query("{ 'owner.user_name' : ?0, 'disable': false, 'active': true }")
    List<VehicleEntity> findAllByActiveIsTrue(String username);
}
