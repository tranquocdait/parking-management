package com.huyendieu.parking.constants;


import java.util.HashMap;
import java.util.Map;

public class Constant {

    public enum Character {
        ;
        public static final String BLANK = "";
        public static final String SPACE = " ";

        public static final String VIRGULE = "/";
    }

    public enum DateTimeFormat {
        ;
        public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final String DD_MM = "dd/MM";
        public static final String FIRST_TIME_OF_DATE = "00:00:00";
        public static final String LAST_TIME_OF_DATE = "23:59:59";
    }

    public enum CheckParkingCode {

        CHECK_IN(1, "CHECK_IN", "Check in successful."),
        CHECK_OUT(2, "CHECK_OUT", "Check out successful.");

        private final Integer key;

        private final String code;

        private final String value;

        CheckParkingCode(Integer key, String code, String value) {
            this.key = key;
            this.code = code;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum DashboardType {
        ;
        public static final int MONTH = 1;
        public static final int YEAR = 2;
    }

    public enum ParkingRegistrationStatus {
        REGISTER(1, "REGISTER", "Register successful."),
        ACCEPT(2, "ACCEPT", "Accept successful."),
        REJECT(3, "REJECT", "Reject successful.");

        private final Integer key;

        private final String code;

        private final String value;

        private Map<Integer, ParkingRegistrationStatus> mapData = new HashMap<>();

        ParkingRegistrationStatus(Integer key, String code, String value) {
            this.key = key;
            this.code = code;
            this.value = value;
            mapData.put(this.key, this);
        }

        public Integer getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static ParkingRegistrationStatus findByKey(Integer key) {
            for (ParkingRegistrationStatus status : values()) {
                if (status.key == key) {
                    return status;
                }
            }
            return null;
        }
    }
}
