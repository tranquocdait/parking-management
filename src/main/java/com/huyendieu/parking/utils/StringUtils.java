package com.huyendieu.parking.utils;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().isEmpty();
    }
}
