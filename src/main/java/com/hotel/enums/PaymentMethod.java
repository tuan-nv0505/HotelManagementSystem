package com.hotel.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    OMO("Ví MoMo"),
    VNPAY("VNPay"),
    CASH("Tiền mặt"),
    BANK_TRANSFER("Chuyển khoản ngân hàng");


    private String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}
