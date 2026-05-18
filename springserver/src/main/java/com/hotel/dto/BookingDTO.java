package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingDTO {

    private Integer id;
    private Integer customerId;
    private String expectedCheckIn;
    private String expectedCheckOut;
    private String actualCheckIn;
    private String actualCheckOut;
    private BigDecimal totalAmount;
    private String specialRequest;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String bookingRooms;
}