package com.hotel.services;

import com.hotel.dto.requestbooking.BookingCancelDTO;
import com.hotel.dto.requestbooking.RequestBookingDTO;
import com.hotel.entity.Booking;

public interface MailService {
    void sendBookingConfirmation(RequestBookingDTO booking);
    void sendBookingCancellationDueToTimeout(BookingCancelDTO booking);
}
