package com.huyendieu.parking.repositories.complex.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.model.request.base.SearchBaseRequestModel;
import com.huyendieu.parking.repositories.complex.VehicleComplexRepository;
import com.huyendieu.parking.utils.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Repository
public class VehicleComplexRepositoryImpl implements VehicleComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<VehicleEntity> findAllByPaging(SearchBaseRequestModel requestModel, List<ObjectId> excludedIds) {
        Query query = makeQuery(requestModel, excludedIds);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "owner.user_name"));
        query.with(pageable);
        return mongoTemplate.find(query, VehicleEntity.class);
    }

    @Override
    public long countAll(SearchBaseRequestModel requestModel, List<ObjectId> excludedIds) {
        Query query = makeQuery(requestModel, excludedIds);
        return mongoTemplate.count(query, VehicleEntity.class);
    }

    private Query makeQuery(SearchBaseRequestModel requestModel, List<ObjectId> excludedIds) {
        Criteria criteria = Criteria.where("is_disable").ne(true);
        if (!CollectionUtils.isEmpty(excludedIds)) {
            criteria.and("_id").nin(excludedIds);
        }
        String keyword = requestModel.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            Criteria keywordCriteria = new Criteria().orOperator(
                    Criteria.where("plate_number").regex(requestModel.getKeyword(), "i"),
                    Criteria.where("owner.user_name").regex(requestModel.getKeyword(), "i"),
                    Criteria.where("vehicle_model").regex(requestModel.getKeyword(), "i"),
                    Criteria.where("vehicle_brand").regex(requestModel.getKeyword(), "i"));
            criteria.andOperator(keywordCriteria);
        }
        return Query.query(criteria);
    }

    @Override
    public List<ParkingHistoryEntity> findAllByPaging(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Query query = makeQuery(userNameOwner, requestModel);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "check_in_date"));
        query.with(pageable);
        return mongoTemplate.find(query, ParkingHistoryEntity.class);
    }

    @Override
    public long countAll(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Query query = makeQuery(userNameOwner, requestModel);
        return mongoTemplate.count(query, ParkingHistoryEntity.class);
    }

    private Query makeQuery(String userNameOwner, TrackingParkingRequestModel requestModel) {
        Criteria criteria = Criteria.where("vehicle.username_owner").is(userNameOwner);
        if (Constant.CheckParkingCode.CHECK_IN.getKey() == requestModel.getType()) {
            criteria.and("check_out_date").in(Arrays.asList(null, ""));

        } else if (Constant.CheckParkingCode.CHECK_OUT.getKey() == requestModel.getType()) {
            criteria.and("check_out_date").nin(Arrays.asList(null, ""));
        }

        return Query.query(criteria);
    }

}
