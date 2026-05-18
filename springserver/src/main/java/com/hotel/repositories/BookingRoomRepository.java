package com.hotel.repositories;

import com.hotel.entity.BookingRoom;

import java.util.List;

public interface BookingRoomRepository {
    List<BookingRoom> getAllBookingRooms();
}
