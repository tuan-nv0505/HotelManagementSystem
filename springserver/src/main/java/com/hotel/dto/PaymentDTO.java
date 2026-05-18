package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDTO {

    private Integer id;
    private Integer bookingId;
    private Integer staffId;
    private String bookingCustomerName;
    private String staffUsername;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentContext;
    private String status;
    private String transactionCode;
    private String note;
    private String createdAt;
}