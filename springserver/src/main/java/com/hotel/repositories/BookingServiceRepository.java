package com.hotel.repositories;

import com.hotel.entity.BookingService;

import java.util.List;

public interface BookingServiceRepository extends BaseRepository<BookingService> {
    boolean existsByService(int serviceId);
    boolean existsByService(List<Integer> listServiceId);
}
