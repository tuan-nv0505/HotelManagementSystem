package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingRoomDTO extends BaseDTO {
    private Integer bookingId;
    private String bookingCustomerName;
    private String roomName;
    private Integer roomId;
    private BigDecimal priceAtBooking;

}
