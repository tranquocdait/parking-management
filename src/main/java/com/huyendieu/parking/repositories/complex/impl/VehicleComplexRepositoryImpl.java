package com.huyendieu.parking.repositories.complex.impl;

import com.huyendieu.parking.entities.VehicleEntity;
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

import java.util.List;

@Repository
public class VehicleComplexRepositoryImpl implements VehicleComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<VehicleEntity> findAllByPaging(SearchBaseRequestModel requestModel, List<ObjectId> excludeIds) {
        Query query = makeQuery(requestModel, excludeIds);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "owner.user_name"));
        query.with(pageable);
        return mongoTemplate.find(query, VehicleEntity.class);
    }

    @Override
    public long countAll(SearchBaseRequestModel requestModel, List<ObjectId> excludeIds) {
        Query query = makeQuery(requestModel, excludeIds);
        return mongoTemplate.count(query, VehicleEntity.class);
    }

    private Query makeQuery(SearchBaseRequestModel requestModel, List<ObjectId> excludeIds) {
        Criteria criteria = Criteria.where("is_disable").ne(true);
        if (!CollectionUtils.isEmpty(excludeIds)) {
            criteria.and("_id").nin(excludeIds);
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

}
