package com.hotel.services.impl;

import com.hotel.converter.BookingServiceConverter;
import com.hotel.dto.BookingServiceDTO;
import com.hotel.repositories.BookingServiceRepository;
import com.hotel.services.BookingServiceService;
import com.hotel.services.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookingServiceServiceImpl implements BookingServiceService {
    @Autowired
    private BookingServiceRepository bookingServiceRepository;
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
    public void addOrUpdate(BookingServiceDTO BookingServiceDTO) {
        this.bookingServiceRepository.addOrUpdate(this.bookingServiceConverter.toBookingServiceEntity(BookingServiceDTO));
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
}
