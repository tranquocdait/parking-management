package com.huyendieu.parking.repositories;

import com.huyendieu.parking.entities.TestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<TestEntity, String> {

}
