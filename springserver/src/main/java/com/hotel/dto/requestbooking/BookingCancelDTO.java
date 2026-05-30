package com.hotel.dto.requestbooking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCancelDTO {
    private String customerName;
    private String customerEmail;
    private String expectedCheckIn;
    private String expectedCheckOut;
    private String totalAmount;
}
