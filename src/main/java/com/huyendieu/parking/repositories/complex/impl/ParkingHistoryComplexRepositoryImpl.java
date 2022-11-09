package com.huyendieu.parking.repositories.complex.impl;

import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.custom.TotalEntity;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.repositories.complex.ParkingHistoryComplexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingHistoryComplexRepositoryImpl implements ParkingHistoryComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ParkingHistoryEntity> findAllByNotCheckOut(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Query query = makeQuery(userNameOwner, requestModel);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "check_in_date"));
        query.with(pageable);
        return mongoTemplate.find(query, ParkingHistoryEntity.class);
    }

    @Override
    public long countAllByNotCheckOut(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Query query = makeQuery(userNameOwner, requestModel);
        return mongoTemplate.count(query, TotalEntity.class);
    }

    private Query makeQuery(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Criteria criteria = Criteria.where("vehicle.username_owner").is(userNameOwner);
        criteria.and("vehicle.plate_number").regex(requestModel.getKeyword(), "i");
//        query.addCriteria(Criteria.where("vehicle.username_owner").regex("userNameOwner",  "i"));
        return Query.query(criteria);
    }

}
