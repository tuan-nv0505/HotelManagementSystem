package com.hotel.services.impl;

import com.hotel.converter.BookingConverter;
import com.hotel.dto.BookingDTO;
import com.hotel.entity.Booking;
import com.hotel.repositories.BookingRepository;
import com.hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingConverter bookingConverter;

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.getAllBookings();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                .withZone(ZoneId.systemDefault());

        List<BookingDTO> bookingDTOs = new ArrayList<>();
        for (Booking b : bookings) {
            BookingDTO bDTO = bookingConverter.toBookingDTO(b);
            bDTO.setCustomerName(b.getCustomer().getName());
            if (b.getActualCheckIn() != null) {
                bDTO.setActualCheckIn(formatter.format(b.getActualCheckIn()));
            } else {
                bDTO.setActualCheckIn("");
            }
            if (b.getActualCheckOut() != null) {
                bDTO.setActualCheckOut(formatter.format(b.getActualCheckOut()));
            } else {
                bDTO.setActualCheckOut("");
            }
            bookingDTOs.add(bDTO);
        }
        return bookingDTOs;
    }
}
