package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.PaymentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findFirstById(ObjectId id);
}
