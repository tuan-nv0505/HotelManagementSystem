package com.hotel.services;

import com.hotel.dto.BookingServiceDTO;

import java.util.List;

public interface BookingServiceService extends BaseService<BookingServiceDTO> {
    boolean existsByService(int serviceId);
    boolean existsByService(List<Integer> listServiceId);
}
