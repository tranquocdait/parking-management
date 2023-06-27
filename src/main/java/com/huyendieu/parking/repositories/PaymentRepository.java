package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
}
