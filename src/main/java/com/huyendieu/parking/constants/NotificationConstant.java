package com.huyendieu.parking.constants;

public class NotificationConstant {
    public static final String NOTIFICATION_PATH = "notification-%s";

    public enum NotificationType {

        CHECK_IN(1, "CHECK_IN", "Check in"),
        CHECK_OUT(2, "CHECK_OUT", "Check out");

        private final Integer key;

        private final String code;

        private final String value;

        NotificationType(Integer key, String code, String value) {
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
