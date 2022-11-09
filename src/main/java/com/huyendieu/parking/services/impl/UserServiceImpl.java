package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.entities.ParkingAreaEntity;
import com.huyendieu.parking.entities.RoleUserEntity;
import com.huyendieu.parking.entities.UserEntity;
import com.huyendieu.parking.entities.VehicleEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.SignUpRequestModel;
import com.huyendieu.parking.model.response.ParkingAreaResponseModel;
import com.huyendieu.parking.model.response.UserResponseModel;
import com.huyendieu.parking.model.response.VehicleResponseModel;
import com.huyendieu.parking.repositories.ParkingAreaRepository;
import com.huyendieu.parking.repositories.RoleUserRepository;
import com.huyendieu.parking.repositories.UserRepository;
import com.huyendieu.parking.repositories.VehicleRepository;
import com.huyendieu.parking.services.UserService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.utils.DateTimeUtils;
import com.huyendieu.parking.utils.MapperUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void signup(SignUpRequestModel requestModel) throws ParkingException {
        UserEntity userEntity = createUserEntity((requestModel));
        if (requestModel.isVehicleOwner()) {
            createVehicleEntity(requestModel, userEntity);
        } else {
            createParkingEntity(requestModel, userEntity);
        }
    }

    @Override
    public UserResponseModel getMyProfile(Authentication authentication) throws ParkingException {
        if (authentication.getPrincipal() == null) {
            throw new ParkingException("authentication don't exist!");
        }
        String username = (String) authentication.getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserNameIgnoreCase(username);
        if (!optionalUserEntity.isPresent()) {
            throw new ParkingException("authentication don't exist!");
        }
        UserEntity userEntity = optionalUserEntity.get();

        UserResponseModel userResponseModel = MapperUtils.map(userEntity, UserResponseModel.class);
        userResponseModel.setUserId(userEntity.getId().toString());
        userResponseModel.setRoleCode(userEntity.getRoleUser() != null
                ? userEntity.getRoleUser().getCode() :
                Constant.BLANK);
        if (PermissionConstant.RoleCode.VEHICLE_OWNER.getCode().equals(userResponseModel.getRoleCode())) {
            userResponseModel.setVehicles(mappingVehicle(userEntity.getId()));
        }
        if (PermissionConstant.RoleCode.PARKING_OWNER.getCode().equals(userResponseModel.getRoleCode())) {
            userResponseModel.setParkingArea(mappingParkingArea(userEntity.getId()));
        }
        return userResponseModel;
    }

    private ParkingAreaResponseModel mappingParkingArea(ObjectId userId) {
        ParkingAreaEntity parkingAreaEntity = parkingAreaRepository.findFirstByOwnerId(userId);
        return parkingAreaEntity != null
                ? MapperUtils.map(parkingAreaEntity, ParkingAreaResponseModel.class)
                : new ParkingAreaResponseModel();
    }

    private List<VehicleResponseModel> mappingVehicle(ObjectId userId) {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByOwnerId(userId);
        List<VehicleResponseModel> vehicleResponseModels = new ArrayList<>();
        if (CollectionUtils.isEmpty(vehicleEntities)) {
            return vehicleResponseModels;
        }
        for (VehicleEntity vehicleEntity : vehicleEntities) {
            VehicleResponseModel vehicleResponseModel = MapperUtils.map(vehicleEntity, VehicleResponseModel.class);
            vehicleResponseModel.setVehicleId(vehicleEntity.getId().toString());
            vehicleResponseModel.setRegisterDate(DateTimeUtils.convertDateFormat(
                    vehicleEntity.getRegisterDate(),
                    Constant.DateTimeFormat.YYYY_MM_DD,
                    Constant.DateTimeFormat.DD_MM_YYYY));
            vehicleResponseModels.add(vehicleResponseModel);
        }
        return vehicleResponseModels;
    }

    private UserEntity createUserEntity(SignUpRequestModel requestModel) throws ParkingException {
        if (userRepository.findByUserNameIgnoreCase(requestModel.getUserName()).isPresent()) {
            throw new ParkingException("Username exists!");
        }
        // get role
        RoleUserEntity roleUserEntity = roleUserRepository.findByCode(
                requestModel.isVehicleOwner() ?
                        PermissionConstant.RoleCode.VEHICLE_OWNER.getCode() :
                        PermissionConstant.RoleCode.PARKING_OWNER.getCode()).get();

        //add admin account
        UserEntity userEntity = UserEntity.builder()
                .userName(requestModel.getUserName())
                .password(passwordEncoder.encode(requestModel.getPassword()))
                .firstName(requestModel.getFirstName())
                .lastName(requestModel.getLastName())
                .lastName(requestModel.getLastName())
                .email(requestModel.getEmail())
                .phoneNumber(requestModel.getPhoneNumber())
                .roleUser(roleUserEntity)
                .createdDate(currentDate())
                .createdBy(getClass().getSimpleName())
                .build();
        userRepository.save(userEntity);

        return userEntity;
    }

    private void createVehicleEntity(SignUpRequestModel requestModel, UserEntity userEntity) throws ParkingException {
        try {
            VehicleEntity vehicleEntity = VehicleEntity.builder()
                    .plateNumber(requestModel.getPlateNumber())
                    .vehicleModel(requestModel.getVehicleModel())
                    .vehicleModel(requestModel.getVehicleModel())
                    .vehicleBrand(requestModel.getVehicleBrand())
                    .registerDate(DateTimeUtils.convertDateFormat(
                            requestModel.getRegisterDate(),
                            Constant.DateTimeFormat.DD_MM_YYYY,
                            Constant.DateTimeFormat.YYYY_MM_DD))
                    .owner(userEntity)
                    .active(true)
                    .createdDate(currentDate())
                    .createdBy(getClass().getSimpleName())
                    .build();
            vehicleRepository.save(vehicleEntity);
        } catch (Exception ex) {
            throw new ParkingException(ex.getMessage());
        }
    }

    private void createParkingEntity(SignUpRequestModel requestModel, UserEntity userEntity) throws ParkingException {
        try {
            ParkingAreaEntity parkingAreaEntity = ParkingAreaEntity.builder()
                    .address(requestModel.getAddress())
                    .province(requestModel.getProvince())
                    .district(requestModel.getDistrict())
                    .commune(requestModel.getCommune())
                    .owner(userEntity)
                    .createdDate(currentDate())
                    .createdBy(getClass().getSimpleName())
                    .build();
            parkingAreaRepository.save(parkingAreaEntity);
        } catch (Exception ex) {
            throw new ParkingException(ex.getMessage());
        }
    }
}
