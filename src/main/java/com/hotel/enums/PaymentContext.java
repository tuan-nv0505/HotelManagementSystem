package com.hotel.enums;

import lombok.Getter;

@Getter
public enum PaymentContext {
    PAYMENT("Thanh toán"),
    REFUND("Hoàn tiền");

    private String paymentContext;

    PaymentContext(String paymentContext) {
        this.paymentContext = paymentContext;
    }

}
