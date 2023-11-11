package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.TicketEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<TicketEntity, String> {
    Optional<TicketEntity> findFirstById(ObjectId id);

    List<TicketEntity> findAllByParkingArea_Id(ObjectId parkingAreaId);
}
