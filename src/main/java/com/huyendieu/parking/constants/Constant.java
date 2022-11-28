package com.huyendieu.parking.constants;

public class Constant {
    public static final String BLANK = "";

    public enum Character {;
        public static final String BLANK = "";
        public static final String SPACE = " ";
    }

    public enum DateTimeFormat {;
        public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
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
}
