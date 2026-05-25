package com.hotel.services;

import com.hotel.dto.BookingDTO;

import java.util.List;

public interface BookingService extends BaseService<BookingDTO> {
    void recalculateTotalAmount(int bookingId);

    void processExpiredBooking(int bookingId);
}
