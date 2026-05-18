package com.hotel.repositories.impl;

import com.hotel.entity.Service;
import com.hotel.repositories.ServiceRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ServiceRepositoryImpl implements ServiceRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Service> listService() {
        Session session = factory.getObject().getCurrentSession();
        return session.createQuery("FROM Service").getResultList();
    }

    @Override
    public void addOrUpdateService(Service service) {
        Session session = this.factory.getObject().getCurrentSession();
        if (service.getId() == null) {
            session.persist(service);
        } else {
            session.merge(service);
        }
    }
}
