package com.hotel.enums;

import lombok.Getter;

@Getter
public enum StatusBooking {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    CHECKED_IN("Đang lưu trú"),
    CHECKED_OUT("Đã trả phòng"),
    CANCELLED("Đã hủy");

    private String statusBooking;

    StatusBooking(String statusBooking) {
        this.statusBooking = statusBooking;
    }

}
