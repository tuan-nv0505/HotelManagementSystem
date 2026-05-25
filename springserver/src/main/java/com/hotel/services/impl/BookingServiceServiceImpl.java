package com.hotel.services.impl;

import com.hotel.converter.BookingServiceConverter;
import com.hotel.dto.BookingServiceDTO;
import com.hotel.entity.Booking;
import com.hotel.entity.BookingService;
import com.hotel.exceptions.NotFoundBookingException;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.BookingServiceRepository;
import com.hotel.repositories.ServiceRepository;
import com.hotel.services.BookingServiceService;
import com.hotel.services.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class BookingServiceServiceImpl implements BookingServiceService {
    @Autowired
    private BookingServiceRepository bookingServiceRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private com.hotel.services.BookingService bookingServiceImpl;

    @Autowired
    private BookingServiceConverter bookingServiceConverter;

    @Override
    public List<BookingServiceDTO> list(Map<String, String> params) {
        return this.bookingServiceConverter.toBookingServiceDTO(this.bookingServiceRepository.list(params));
    }

    @Override
    public long count(Map<String, String> params) {
        return this.bookingServiceRepository.count(params);
    }

    @Override
    public void addOrUpdate(BookingServiceDTO bookingServiceDTO) {
        int bookingId = bookingServiceDTO.getBookingId();
        Booking booking = this.bookingRepository.get(bookingId);
        if (booking == null) {
            throw new NotFoundBookingException(String.format("Not found Booking: %d", bookingId));
        }

        int serviceId = bookingServiceDTO.getServiceId();
        com.hotel.entity.Service service = this.serviceRepository.get(serviceId);
        if (service == null) {
            throw new NotFoundBookingException(String.format("Not found Service: %d", serviceId));
        }

        BigDecimal priceAtUsage = service.getPrice();

        BookingService bookingService = this.bookingServiceConverter.toBookingServiceEntity(bookingServiceDTO);
        bookingService.setBooking(booking);
        bookingService.setService(service);
        bookingService.setPriceAtUsage(priceAtUsage);
        bookingService.setCreatedAt(Instant.now());

        this.bookingServiceRepository.addOrUpdate(bookingService);
        bookingServiceImpl.recalculateTotalAmount(bookingServiceDTO.getBookingId());
    }

    @Override
    public void delete(int id) {
        this.bookingServiceRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.bookingServiceRepository.delete(ids);
    }

    @Override
    public BookingServiceDTO get(int id) {
        return this.bookingServiceConverter.toBookingServiceDTO(this.bookingServiceRepository.get(id));
    }

    @Override
    public BookingServiceDTO save(BookingServiceDTO entity) {
        return null;
    }

    @Override
    public boolean existsByService(int serviceId) {
        return this.bookingServiceRepository.existsByService(serviceId);
    }

    @Override
    public boolean existsByService(List<Integer> listServiceId) {
        return this.bookingServiceRepository.existsByService(listServiceId);
    }
}
