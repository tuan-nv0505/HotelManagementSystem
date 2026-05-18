package com.hotel.repositories.impl;

import com.hotel.entity.BookingRoom;
import com.hotel.repositories.BookingRoomRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BookingRoomRepositoryImpl implements BookingRoomRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<BookingRoom> getAllBookingRooms() {
        Session session = factory.getObject().getCurrentSession();

        return session.createQuery("FROM BookingRoom", BookingRoom.class).getResultList();
    }
}
