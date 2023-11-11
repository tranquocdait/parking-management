package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.VehicleEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends MongoRepository<VehicleEntity, String> {

    Optional<VehicleEntity> findFirstById(ObjectId id);

    @Query("{ '_id': ?0, 'owner.user_name' : ?1}")
    VehicleEntity findByIdAndUsername(ObjectId id, String userName);

    @Query("{ 'owner.id' : ?0, 'is_disable': false }")
    List<VehicleEntity> findAllByOwnerId(ObjectId id);

    @Query("{ 'owner.user_name' : ?0, 'is_disable': false, 'is_active': true }")
    List<VehicleEntity> findAllByActiveIsTrue(String username);
}
