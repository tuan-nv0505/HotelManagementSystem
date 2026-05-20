package com.hotel.enums;

import lombok.Getter;

@Getter
public enum AvailabilityStatus {
    READY("Sẵn sàng"),
    MAINTENANCE("Bảo trì");

    private final String value;

    AvailabilityStatus(String value) {
        this.value = value;
    }
}
