package com.huyendieu.parking.constants;

public class PermissionConstant {

    public static String AUTHORIZATION_HEADER = "Authorization";

    public static final long EXPIRATION_TIME = 1000 * 3600 * 24; // 1 day

    public static String LOGIN_URI = "/login";

    public enum RoleCode {

        ADMIN(1, "ADMIN", "ROLE_ADMIN", "Admin"),
        PARKING_OWNER(2, "PARKING_OWNER", "ROLE_PARKING_OWNER", "Parking owner"),
        VEHICLE_OWNER(3,"VEHICLE_OWNER", "ROLE_PARKING_OWNER","Vehicle owner");

        private final Integer key;

        private final String code;

        private final String securityCode;

        private final String name;

        RoleCode(Integer key, String code, String name, String securityCode) {
            this.key = key;
            this.code = code;
            this.securityCode = securityCode;
            this.name = name;
        }

        public Integer getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }

        public String getSecurityCode() {
            return securityCode;
        }

        public String getName() {
            return name;
        }
    }
}
