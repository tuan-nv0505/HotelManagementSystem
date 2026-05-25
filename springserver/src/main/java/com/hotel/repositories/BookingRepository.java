package com.hotel.repositories;

import com.hotel.entity.Booking;

import java.util.List;

public interface BookingRepository extends BaseRepository<Booking> {
    void flush();
    List<Booking> findExpiredBookings(int minutes);
}
