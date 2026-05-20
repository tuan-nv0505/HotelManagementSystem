package com.hotel.repositories;

import com.hotel.entity.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> getAllBookings();

}
