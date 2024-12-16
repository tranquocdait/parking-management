package com.huyendieu.parking.repositories.complex.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.PaymentEntity;
import com.huyendieu.parking.model.dto.DashboardModel;
import com.huyendieu.parking.model.request.SearchPaymentRequestModel;
import com.huyendieu.parking.repositories.complex.PaymentComplexRepository;
import com.huyendieu.parking.services.base.BaseService;
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

import java.util.List;

@Repository
public class PaymentComplexRepositoryImpl extends BaseService implements PaymentComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PaymentEntity> findAll(String userNameOwner, DashboardModel requestModel) {
        Criteria criteria = Criteria.where("parking_area.username_owner").is(userNameOwner);
        if (!StringUtils.isEmpty(requestModel.getDateFrom()) && !StringUtils.isEmpty(requestModel.getDateTo())) {
            criteria.and("start_date").gte(requestModel.getDateFrom()).lte(requestModel.getDateTo());
        } else if (!StringUtils.isEmpty(requestModel.getDateTo())) {
            criteria.and("start_date").lte(requestModel.getDateTo());
        } else if (!StringUtils.isEmpty(requestModel.getDateFrom())) {
            criteria.and("start_date").gte(requestModel.getDateFrom());
        }
        return mongoTemplate.find(Query.query(criteria), PaymentEntity.class);
    }

    @Override
    public List<PaymentEntity> findAllByPaging(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea) {
        Query query = makeQuery(requestModel, userName, isParkingArea);
        Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getPageSize(),
                Sort.by(Sort.Direction.DESC, "start_date", "is_active"));
        query.with(pageable);
        return mongoTemplate.find(query, PaymentEntity.class);
    }

    @Override
    public List<PaymentEntity> findValidPaymentByUser(String username, String parkingAreaId, ObjectId ticketId) {
        Criteria criteria = Criteria.where("parking_area._id").is(new ObjectId(parkingAreaId))
                .and("vehicle.username_owner").is(username)
                .and("ticket._id").is(ticketId)
                .and("is_active").is(true)
                .and("status").is(Constant.PaymentStatus.DONE.getKey())
                .and("end_date").gte(currentDate());
        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "end_date"));
        return mongoTemplate.find(query, PaymentEntity.class);
    }

    @Override
    public long countAll(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea) {
        Query query = makeQuery(requestModel, userName, isParkingArea);
        return mongoTemplate.count(query, PaymentEntity.class);
    }

    private Query makeQuery(SearchPaymentRequestModel requestModel, String userName, boolean isParkingArea) {
        Criteria criteria;
        if (isParkingArea) {
            criteria = Criteria.where("parking_area.username_owner").is(userName);
        } else {
            criteria = Criteria.where("vehicle.username_owner").is(userName);
        }

        Boolean isActive = requestModel.getIsActive();
        if (isActive != null) {
            criteria.and("is_active").is(isActive);
        }
        return Query.query(criteria);
    }

}
