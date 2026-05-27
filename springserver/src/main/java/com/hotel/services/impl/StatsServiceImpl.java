package com.hotel.services.impl;

import com.hotel.repositories.RoomRepository;
import com.hotel.repositories.StatsRepository;
import com.hotel.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Object[]> getRevenue(String type, int year) {
        if ("month".equals(type)) return statsRepository.getRevenueByMonth(year);
        if ("quarter".equals(type)) return statsRepository.getRevenueByQuarter(year);
        return statsRepository.getRevenueByYear();
    }

    @Override
    public double getOccupancyRate(LocalDate date) {
        long occupied = statsRepository.getOccupiedRoomsCount(date);
        long total = roomRepository.countRentableRooms();
        if (total == 0) return 0.0;
        return Math.round(((double) occupied / total) * 10000.0) / 100.0;
    }
}