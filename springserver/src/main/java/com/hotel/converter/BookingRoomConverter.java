package com.hotel.converter;

import com.hotel.dto.BookingRoomDTO;
import com.hotel.entity.BookingRoom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingRoomConverter {
    @Autowired
    private ModelMapper modelMapper;

    public BookingRoomDTO toBookingRoomDTO(BookingRoom bookingRoom) {
        return modelMapper.map(bookingRoom, BookingRoomDTO.class);
    }

    public BookingRoom toBookingRoom(BookingRoomDTO bookingRoomDTO) {
        return modelMapper.map(bookingRoomDTO, BookingRoom.class);
    }
}
