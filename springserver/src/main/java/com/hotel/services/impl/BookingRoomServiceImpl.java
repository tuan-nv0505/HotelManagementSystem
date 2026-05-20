package com.hotel.services.impl;

import com.hotel.converter.BookingRoomConverter;
import com.hotel.dto.BookingRoomDTO;
import com.hotel.dto.BookingRoomDTO;
import com.hotel.entity.Booking;
import com.hotel.entity.BookingRoom;
import com.hotel.entity.Customer;
import com.hotel.entity.Room;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.BookingRoomRepository;
import com.hotel.repositories.RoomRepository;
import com.hotel.services.BookingRoomService;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BookingRoomServiceImpl implements BookingRoomService {
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private BookingRoomConverter bookingRoomConverter;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<BookingRoomDTO> list(Map<String, String> params) {
        List<BookingRoom> bookingList = bookingRoomRepository.list(params);
        List<BookingRoomDTO> listBooking = new ArrayList<>();

        for (BookingRoom b : bookingList) {
            BookingRoomDTO bookingRoomDTO = bookingRoomConverter.toBookingRoomDTO(b);
            if (b.getRoom() != null) {
                bookingRoomDTO.setRoomName(b.getRoom().getRoomNumber());
            }
            if (b.getBooking() != null && b.getBooking().getCustomer() != null) {
                bookingRoomDTO.setBookingCustomerName(b.getBooking().getCustomer().getName());
            }

            listBooking.add(bookingRoomDTO);
        }
        return listBooking;
    }

    @Override
    public long count(Map<String, String> params) {
        return bookingRoomRepository.count(params);
    }

    @Override
    public void addOrUpdate(BookingRoomDTO bookingRoomDTO) {
        BookingRoom bookingRoom = bookingRoomConverter.toBookingRoom(bookingRoomDTO);
        Booking booking = bookingRepository.get(bookingRoomDTO.getBookingId());
        Room room = roomRepository.get(bookingRoomDTO.getRoomId());
        if (booking != null && room != null) {
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
        } else {
            throw new RuntimeException("Không tìm thấy Đơn đặt phòng hoặc Phòng tương ứng!");
        }
        this.bookingRoomRepository.addOrUpdate(bookingRoom);
    }

    @Override
    public void delete(int id) {
        this.bookingRoomRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.bookingRoomRepository.delete(ids);
    }

    @Override
    public BookingRoomDTO get(int id) {
        return null;
    }
}
