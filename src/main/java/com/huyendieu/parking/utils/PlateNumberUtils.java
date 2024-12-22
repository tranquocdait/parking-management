package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.Constant;


public class PlateNumberUtils {
    public static String shortedPlateNumber(String originalPlateNumber) {
        try {
            return originalPlateNumber.replaceAll("[^a-zA-Z0-9]", "");
        } catch (Exception e) {
            return Constant.Character.BLANK;
        }
    }
}
