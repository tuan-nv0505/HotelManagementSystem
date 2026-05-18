package com.hotel.enums;

import lombok.Getter;

@Getter
public enum RoleUser {
    ROLE_ADMIN("Quản trị viên"),
    ROLE_STAFF("Nhân viên Lễ tân"),
    ROLE_HOUSEKEEPING("Nhân viên Buồng phòng"),
    ROLE_CUSTOMER("Khách hàng");

    private String role;

    RoleUser(String role) {
        this.role = role;
    }

    public static String getValue(String roleUser) {
        for (RoleUser role : RoleUser.values()) {
            if (role.toString().equals(roleUser)) {
                return role.getRole();
            }
        }

        return null;
    }
}
