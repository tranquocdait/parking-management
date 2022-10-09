package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUntil {

    public static String convertDateTimeFormat(String datetime, String formatFrom, String formatTo) {
        DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatterFrom);

        // desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
        return localDateTime.format(formatter);
    }


}
