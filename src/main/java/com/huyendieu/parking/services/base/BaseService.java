package com.huyendieu.parking.services.base;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.utils.DateTimeUtils;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;

public class BaseService {

    protected String currentDate() {

        return DateTimeUtils.convertDateTimeFormat(LocalDateTime.now(), Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS);
    }
}
