package com.huyendieu.parking.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.huyendieu.parking.entities.TestEntity;

public interface ITestRepository extends MongoRepository<TestEntity, String>{
    
}
