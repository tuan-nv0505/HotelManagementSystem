package com.hotel.services;

import com.hotel.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    public List<BookingDTO> getAllBookings();
}
