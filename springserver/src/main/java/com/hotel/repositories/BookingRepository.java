package com.hotel.repositories;

import com.hotel.dto.requestbooking.RequestBookingDTO;
import com.hotel.entity.Booking;

import java.util.List;

public interface BookingRepository extends BaseRepository<Booking> {
    void flush();

    List<Booking> findExpiredBookings(int minutes);

    Booking addOrUpdateGetObject(Booking booking);

    void processExpiredBooking(Integer bookingId);
}
