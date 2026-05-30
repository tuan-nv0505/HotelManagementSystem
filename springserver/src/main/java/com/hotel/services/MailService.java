package com.hotel.services;

import com.hotel.dto.requestbooking.RequestBookingDTO;

public interface MailService {
    void sendBookingConfirmation(RequestBookingDTO booking);
    void sendBookingCancellationDueToTimeout(RequestBookingDTO booking);
}
