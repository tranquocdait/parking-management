package com.huyendieu.parking.repositories.complex.impl;

import com.huyendieu.parking.entities.TicketEntity;
import com.huyendieu.parking.model.request.SearchTicketRequestModel;
import com.huyendieu.parking.repositories.complex.TicketComplexRepository;
import com.huyendieu.parking.utils.StringUtils;
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
public class TicketComplexRepositoryImpl implements TicketComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<TicketEntity> findAllByPaging(SearchTicketRequestModel requestModel, String userName) {
        Query query = makeQuery(requestModel, userName);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "created_date", "is_active"));
        query.with(pageable);
        return mongoTemplate.find(query, TicketEntity.class);
    }

    @Override
    public long countAll(SearchTicketRequestModel requestModel, String userName) {
        Query query = makeQuery(requestModel, userName);
        return mongoTemplate.count(query, TicketEntity.class);
    }

    private Query makeQuery(SearchTicketRequestModel requestModel, String userName) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(userName)) {
            criteria.and("parking_area.username_owner").is(userName);
        }
        int type = requestModel.getType();
        if (type != 0) {
            criteria.and("type").is(type);
        }
        return Query.query(criteria);
    }

}
