package com.hotel.services;

import com.hotel.dto.BookingDTO;
import com.hotel.dto.requestbooking.RequestBookingDTO;

import java.util.List;

public interface BookingService extends BaseService<BookingDTO> {
    void recalculateTotalAmount(int bookingId);
    Integer processAddBooking(RequestBookingDTO dto);
}
