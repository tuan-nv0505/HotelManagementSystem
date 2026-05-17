package com.hotel.enums;

import lombok.Getter;

@Getter
public enum StatusPayment {
    PENDING("Chờ thanh toán"),
    COMPLETED("Thành công"),
    FAILED("Thất bại");

    private String statusPayment;

    StatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }


}
