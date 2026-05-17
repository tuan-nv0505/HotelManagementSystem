package com.hotel.enums;

import lombok.Getter;

@Getter
public enum AvailabilityStatus {
    READY("Sẵn sàng"),
    MAINTENANCE("Bảo trì");

    private String availabilityStatus;

    AvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }


}
