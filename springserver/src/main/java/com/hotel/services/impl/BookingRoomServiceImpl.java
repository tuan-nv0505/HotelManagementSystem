package com.hotel.services.impl;

import com.hotel.converter.BookingRoomConverter;
import com.hotel.dto.BookingRoomDTO;
import com.hotel.entity.BookingRoom;
import com.hotel.repositories.BookingRoomRepository;
import com.hotel.services.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookingRoomServiceImpl implements BookingRoomService {
    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    @Autowired
    private BookingRoomConverter bookingRoomConverter;

    @Override
    public List<BookingRoomDTO> getAllBookingRooms() {
        List<BookingRoom> bookingRooms = bookingRoomRepository.getAllBookingRooms();

        List<BookingRoomDTO> bookingRoomDTOList = new ArrayList<>();
        for (BookingRoom bookingRoom : bookingRooms) {
            BookingRoomDTO bookingRoomDTO = bookingRoomConverter.toBookingRoomDTO(bookingRoom);
            bookingRoomDTO.setBookingCustomerName(bookingRoom.getBooking().getCustomer().getName());
            bookingRoomDTO.setRoomName(bookingRoom.getRoom().getRoomNumber());

            bookingRoomDTOList.add(bookingRoomDTO);
        }

        return bookingRoomDTOList;
    }
}
