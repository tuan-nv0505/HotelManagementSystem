package com.hotel.enums;

import lombok.Getter;

@Getter
public enum StatusRoom {
    VACANT_CLEAN("Trống - Sạch"),
    VACANT_DIRTY("Trống - Bẩn"),
    OCCUPIED_CLEAN("Có khách - Sạch"),
    OCCUPIED_DIRTY("Có khách - Bẩn");

    private String statusRoom;

    StatusRoom(String statusRoom) {
        this.statusRoom = statusRoom;
    }

}
