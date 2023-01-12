package com.huyendieu.parking.repositories.complex;

import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.model.request.base.SearchBaseRequestModel;
import org.bson.types.ObjectId;

import java.util.List;

public interface VehicleComplexRepository {

    List<VehicleEntity> findAllByPaging(SearchBaseRequestModel requestModel, List<ObjectId> excludeIds);

    long countAll(SearchBaseRequestModel requestModel, List<ObjectId> excludeIds);
}
