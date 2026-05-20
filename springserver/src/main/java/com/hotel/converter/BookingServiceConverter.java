package com.hotel.converter;

import com.hotel.dto.BookingServiceDTO;
import com.hotel.entity.Booking;
import com.hotel.entity.BookingService;
import com.hotel.entity.Service;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingServiceConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public BookingService toBookingServiceEntity(BookingServiceDTO bookingServiceDTO) {
        BookingService bookingService = this.modelMapper.map(bookingServiceDTO, BookingService.class);

        Service serviceEntity = this.serviceRepository.get(bookingServiceDTO.getServiceId());
        Booking bookingEntity = this.bookingRepository.get(bookingServiceDTO.getBookingId());

        bookingService.setService(serviceEntity);
        bookingService.setBooking(bookingEntity);
        bookingService.setPriceAtUsage(serviceEntity.getPrice());
        bookingService.setCreatedAt(Instant.now());


        return bookingService;
    }

    public BookingServiceDTO toBookingServiceDTO(BookingService bookingServiceEntity) {
        BookingServiceDTO BookingServiceDTO = this.modelMapper.map(bookingServiceEntity, BookingServiceDTO.class);
        BookingServiceDTO.setServiceId(bookingServiceEntity.getService().getId());
        BookingServiceDTO.setServiceName(bookingServiceEntity.getService().getName());
        return BookingServiceDTO;
    }

    public List<BookingServiceDTO> toBookingServiceDTO(List<BookingService> listBookingServiceEntity) {
        return listBookingServiceEntity.stream().map(
                element -> this.toBookingServiceDTO(element)
        ).collect(Collectors.toList());
    }
}