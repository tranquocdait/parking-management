package com.huyendieu.parking.services;

import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.SignUpRequestModel;
import com.huyendieu.parking.model.response.UserResponseModel;
import org.springframework.security.core.Authentication;

public interface UserService {
    void signup(SignUpRequestModel requestModel) throws ParkingException;

    UserResponseModel getMyProfile(Authentication authentication) throws ParkingException;
}
