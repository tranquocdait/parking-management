package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.TicketEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<TicketEntity, String> {
    List<TicketEntity> findAllByParkingArea_Id(ObjectId parkingAreaId);
}
