package com.huyendieu.parking.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.huyendieu.parking.entities.ParkingHistoryEntity;

public interface ParkingHistoryRepository extends MongoRepository<ParkingHistoryEntity, String> {

    @Query("{ 'vehicle.username_owner' : ?0, 'parking_area._id' : ?1, 'check_out_date': { '$in': [ null, '' ] } }")
    List<ParkingHistoryEntity> findUserByNotCheckOut(String username, ObjectId parkingAreaId);
}
