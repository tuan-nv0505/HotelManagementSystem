package com.hotel.repositories.impl;

import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomTypeRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RoomTypeRepositoryImpl implements RoomTypeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<RoomType> listRoomType() {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery("FROM RoomType ", RoomType.class).getResultList();
    }
}
