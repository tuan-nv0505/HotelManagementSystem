package com.hotel.repositories.impl;

import com.hotel.entity.Booking;
import com.hotel.repositories.BookingRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Booking> getAllBookings() {
        Session session = factory.getObject().getCurrentSession();
        return session.createQuery("from Booking", Booking.class).getResultList();
    }
}
