package com.hotel.converter;

import com.hotel.dto.BookingDTO;
import com.hotel.entity.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter {
    @Autowired
    private ModelMapper modelMapper;

    public BookingDTO toBookingDTO(Booking booking) {
        return modelMapper.map(booking, BookingDTO.class);
    }

    public Booking toBooking(BookingDTO bookingDTO) {
        return modelMapper.map(bookingDTO, Booking.class);
    }
}
