package com.hotel.repositories.impl;

import com.hotel.entity.Customer;
import com.hotel.repositories.CustomerRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Customer> getAllCustomers() {
        Session session = factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM Customer", Customer.class);

        return query.getResultList();
    }
}
