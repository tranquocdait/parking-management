package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.exception.ParkingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String convertDateFormat(LocalDate datetime, String formatTo) {
        try {
            // desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
            return datetime.format(formatter);
        } catch (Exception ex) {
            return Constant.Character.BLANK;
        }
    }

    public static String convertDateTimeFormat(String datetime, String formatFrom, String formatTo) {
        if (StringUtils.isEmpty(datetime)) {
            return Constant.Character.BLANK;
        }
        try {
            DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
            LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatterFrom);

            // desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
            return localDateTime.format(formatter);

        } catch (Exception ex) {
            return Constant.Character.BLANK;
        }
    }

    public static String convertDateTimeFormat(LocalDateTime datetime, String formatTo) {
        try {
            // desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
            return datetime.format(formatter);
        } catch (Exception ex) {
            return Constant.Character.BLANK;
        }
    }

    public static String convertDateFormat(String date, String formatFrom, String formatTo) {
        if (StringUtils.isEmpty(date)) {
            return Constant.Character.BLANK;
        }
        try {
            DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
            LocalDate localDateTime = LocalDate.parse(date, formatterFrom);

            // desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
            return localDateTime.format(formatter);
        } catch (Exception ex) {
            return Constant.Character.BLANK;
        }
    }

    public static LocalDate convertStringToDate(String date, String formatFrom) throws ParkingException {
        if (StringUtils.isEmpty(date)) {
            throw new ParkingException("date is not null");
        }
        try {
            DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
            return LocalDate.parse(date, formatterFrom);
        } catch (Exception ex) {
            throw new ParkingException(ex.getMessage());
        }
    }

    public static LocalDateTime convertStringToDateTime(String date, String formatFrom) throws ParkingException {
        if (StringUtils.isEmpty(date)) {
            throw new ParkingException("date is not null");
        }
        try {
            DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
            return LocalDateTime.parse(date, formatterFrom);
        } catch (Exception ex) {
            throw new ParkingException(ex.getMessage());
        }
    }
}
