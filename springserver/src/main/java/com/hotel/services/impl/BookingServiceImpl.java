package com.hotel.services.impl;

import com.hotel.converter.BookingConverter;
import com.hotel.dto.BookingDTO;
import com.hotel.entity.Booking;
import com.hotel.entity.BookingRoom;
import com.hotel.entity.Customer;
import com.hotel.exceptions.NotFoundBookingException;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.CustomerRepository;
import com.hotel.repositories.RoomInventoryRepository;
import com.hotel.services.BookingService;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingConverter bookingConverter;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomInventoryRepository roomInventoryRepository;

    @Override
    public List<BookingDTO> list(Map<String, String> params) {
        List<Booking> bookingList = bookingRepository.list(params);
        List<BookingDTO> listBooking = new ArrayList<>();

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

        for (Booking b : bookingList) {
            BookingDTO bookingDTO = bookingConverter.toBookingDTO(b);
            bookingDTO.setCustomerName(b.getCustomer().getName());
            if (b.getCustomer() != null) {
                bookingDTO.setCustomerName(b.getCustomer().getName());
                bookingDTO.setCustomerId(b.getCustomer().getId());
            }
            if (b.getActualCheckIn() != null) {
                bookingDTO.setActualCheckInDisplay(b.getActualCheckIn().format(displayFormatter));
            } else {
                bookingDTO.setActualCheckInDisplay("Chưa nhận phòng");
            }
            if (b.getActualCheckOut() != null) {
                bookingDTO.setActualCheckOutDisplay(b.getActualCheckOut().format(displayFormatter));
            } else {
                bookingDTO.setActualCheckOutDisplay("Chưa trả phòng");
            }

            listBooking.add(bookingDTO);
        }
        return listBooking;
    }

    @Override
    public long count(Map<String, String> params) {
        return bookingRepository.count(params);
    }

    @Override
    public void addOrUpdate(BookingDTO bookingDTO) {
        Booking booking = bookingConverter.toBooking(bookingDTO);
        if (bookingDTO.getId() == null) {
            booking.setTotalAmount(BigDecimal.valueOf(0.0));
        }
        Customer customer = null;
        try {
            customer = customerRepository.getCustomerByName(bookingDTO.getCustomerName());
        } catch (NoResultException ex) {
            throw new RuntimeException("Tên khách hàng '" + bookingDTO.getCustomerName() + "' không tồn tại trên hệ thống. Vui lòng kiểm tra lại!");
        }
        booking.setCustomer(customer);

        this.bookingRepository.addOrUpdate(booking);
    }

    @Override
    public void delete(int id) {
        this.bookingRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.bookingRepository.delete(ids);
    }

    @Override
    public BookingDTO get(int id) {
        Booking booking = this.bookingRepository.get(id);
        if (booking == null) {
            throw new NotFoundBookingException(String.format("Not found Booking ID: %d", id));
        }
        return this.bookingConverter.toBookingDTO(booking);
    }

    @Override
    public BookingDTO save(BookingDTO entity) {
        return null;
    }

    @Override
    public void recalculateTotalAmount(int bookingId) {
        Booking booking = bookingRepository.get(bookingId);
        if (booking == null) return;

        BigDecimal roomTotal = BigDecimal.ZERO;
        if (booking.getBookingRooms() != null) {
            for (BookingRoom br : booking.getBookingRooms()) {
                roomTotal = roomTotal.add(br.getPriceAtBooking());
            }
        }

        BigDecimal serviceTotal = BigDecimal.ZERO;
        if (booking.getBookingServices() != null) {
            for (com.hotel.entity.BookingService bs : booking.getBookingServices()) {
                BigDecimal quantity = BigDecimal.valueOf(bs.getQuantity());
                BigDecimal subTotal = bs.getPriceAtUsage().multiply(quantity);
                serviceTotal = serviceTotal.add(subTotal);
            }
        }

        BigDecimal totalAmount = roomTotal.add(serviceTotal);
        booking.setTotalAmount(totalAmount);
        bookingRepository.addOrUpdate(booking);
    }
}
