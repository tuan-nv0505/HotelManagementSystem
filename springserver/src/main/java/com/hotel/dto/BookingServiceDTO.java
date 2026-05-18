package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingServiceDTO {

    private Integer id;
    private Integer bookingId;
    private Integer serviceId;
    private Integer quantity;
    private BigDecimal priceAtUsage;
    private String note;
}