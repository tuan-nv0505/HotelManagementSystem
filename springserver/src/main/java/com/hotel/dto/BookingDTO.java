package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDTO extends BaseDTO {
    private Integer customerId;
    private String customerName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedCheckIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedCheckOut;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime actualCheckIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime actualCheckOut;
    private String actualCheckInDisplay;
    private String actualCheckOutDisplay;
    private BigDecimal totalAmount;
    private String specialRequest;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String bookingRooms;
}