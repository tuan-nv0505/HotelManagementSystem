package com.hotel.repositories;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepository {
    List<Object[]> getRevenueByMonth(int year);

    List<Object[]> getRevenueByQuarter(int year);

    List<Object[]> getRevenueByYear();

    long getOccupiedRoomsCount(LocalDate date);
}