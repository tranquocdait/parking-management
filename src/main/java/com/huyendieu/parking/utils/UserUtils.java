package com.huyendieu.parking.utils;

import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.exception.ParkingException;
import org.springframework.security.core.Authentication;

public class UserUtils {

    public static String getUserName(Authentication authentication) throws ParkingException {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new ParkingException("authentication don't exist!");
        }
        return authentication.getPrincipal().toString();
    }

    public static String getUserRole(Authentication authentication) throws ParkingException {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new ParkingException("authentication don't exist!");
        }
        return authentication.getAuthorities().toString();
    }

    public static boolean isVehicleRole(Authentication authentication) throws ParkingException {
        String authority = getUserRole(authentication);
        return authority.contains(PermissionConstant.RoleCode.VEHICLE_OWNER.getCode());
    }

    public static boolean isParkingAreaRole(Authentication authentication) throws ParkingException {
        String authority = getUserRole(authentication);
        return authority.contains(PermissionConstant.RoleCode.PARKING_OWNER.getCode());
    }
}
