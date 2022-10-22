package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.Constant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String convertDateTimeFormat(String datetime, String formatFrom, String formatTo) {
        DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatterFrom);

        // desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
        return localDateTime.format(formatter);
    }

    public static String convertDateTimeFormat(LocalDateTime datetime, String formatTo) {

        // desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
        return datetime.format(formatter);
    }

    public static String convertDateFormat(String date, String formatFrom, String formatTo) {
        DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
        LocalDate localDateTime = LocalDate.parse(date, formatterFrom);

        // desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTo);
        return localDateTime.format(formatter);
    }

}
