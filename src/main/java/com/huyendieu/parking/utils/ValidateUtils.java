package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.Constant;

import java.time.format.DateTimeFormatter;

public class ValidateUtils {

    public static boolean isDateValid(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            formatter.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDateValid(String date) {
        return isDateValid(date, Constant.DateTimeFormat.DD_MM_YYYY);
    }

    public static boolean isDateTimeValid(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            formatter.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDateTimeValid(String date) {
        return isDateTimeValid(date, Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS);
    }
}
