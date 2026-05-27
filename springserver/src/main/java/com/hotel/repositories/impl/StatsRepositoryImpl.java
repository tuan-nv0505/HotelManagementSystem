package com.hotel.repositories.impl;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingRoom;
import com.hotel.entity.Payment;
import com.hotel.repositories.StatsRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> getRevenueByMonth(int year) {
        Session session = factory.getObject().getCurrentSession();
        String sql = "SELECT MONTH(created_at) as month, SUM(amount) as total " +
                "FROM payment " +
                "WHERE status = 'COMPLETED' AND YEAR(created_at) = :year " +
                "GROUP BY MONTH(created_at) " +
                "ORDER BY month";

        return session.createNativeQuery(sql, Object[].class)
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public List<Object[]> getRevenueByQuarter(int year) {
        Session session = factory.getObject().getCurrentSession();
        String sql = "SELECT QUARTER(created_at) as quarter, SUM(amount) as total " +
                "FROM payment " +
                "WHERE status = 'COMPLETED' AND YEAR(created_at) = :year " +
                "GROUP BY QUARTER(created_at) " +
                "ORDER BY quarter";

        return session.createNativeQuery(sql, Object[].class)
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public List<Object[]> getRevenueByYear() {
        Session session = factory.getObject().getCurrentSession();
        String sql = "SELECT YEAR(created_at) as year, SUM(amount) as total " +
                "FROM payment " +
                "WHERE status = 'COMPLETED' " +
                "GROUP BY YEAR(created_at) " +
                "ORDER BY year DESC";

        return session.createNativeQuery(sql, Object[].class)
                .getResultList();
    }

    @Override
    public long getOccupiedRoomsCount(LocalDate date) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);


        Root<BookingRoom> root = query.from(BookingRoom.class);
        Join<BookingRoom, Booking> bookingJoin = root.join("booking");

        query.select(builder.count(root.get("id")));

        Predicate pStatus = bookingJoin.get("status").in(Arrays.asList("CONFIRMED", "CHECKED_IN"));
        Predicate pCheckIn = builder.lessThanOrEqualTo(bookingJoin.get("expectedCheckIn"), date);
        Predicate pCheckOut = builder.greaterThan(bookingJoin.get("expectedCheckOut"), date);

        query.where(builder.and(pStatus, pCheckIn, pCheckOut));

        Long count = session.createQuery(query).uniqueResult();
        return count != null ? count : 0L;
    }
}
