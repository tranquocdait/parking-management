package com.huyendieu.parking.repositories.complex.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.huyendieu.parking.constants.Constant.CheckParkingCode;
import com.huyendieu.parking.entities.ParkingHistoryEntity;
import com.huyendieu.parking.model.request.TrackingParkingRequestModel;
import com.huyendieu.parking.repositories.complex.ParkingHistoryComplexRepository;
import com.huyendieu.parking.utils.StringUtils;

@Repository
public class ParkingHistoryComplexRepositoryImpl implements ParkingHistoryComplexRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ParkingHistoryEntity> findAll(String userNameOwner, TrackingParkingRequestModel requestModel) {
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
		Criteria criteria = Criteria.where("parking_area.username_owner").is(userNameOwner);
		if (CheckParkingCode.CHECK_IN.getKey() == requestModel.getType()) {
			criteria.and("check_out_date").in(Arrays.asList(null, ""));
			
		} else if (CheckParkingCode.CHECK_OUT.getKey() == requestModel.getType()) {
			criteria.and("check_out_date").nin(Arrays.asList(null, ""));
		}
		String keyword = requestModel.getKeyword();
		if (!StringUtils.isEmpty(keyword)) {
			Criteria keywordCriteria = new Criteria().orOperator(
					Criteria.where("vehicle.plate_number").regex(requestModel.getKeyword(), "i"),
					Criteria.where("vehicle.username_owner").regex(requestModel.getKeyword(), "i"),
					Criteria.where("vehicle.vehicle_model").regex(requestModel.getKeyword(), "i"),
					Criteria.where("vehicle.vehicle_brand").regex(requestModel.getKeyword(), "i"));
			criteria.andOperator(keywordCriteria);
		}
		return Query.query(criteria);
	}

}
