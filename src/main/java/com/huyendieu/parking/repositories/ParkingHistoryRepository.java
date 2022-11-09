package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ParkingHistoryRepository extends MongoRepository<ParkingHistoryEntity, String> {

    @Query("{ 'vehicle.username_owner' : ?0, 'parking_area.id' : ?1, 'check_out_date': { '$nin': [ null, '' ] } }")
    List<ParkingHistoryEntity> findUserByNotCheckOut(String username, ObjectId parkingAreaId);
}
