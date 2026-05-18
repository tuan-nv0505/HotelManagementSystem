package com.hotel.repositories.impl;

import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.enums.RoleUser;
import com.hotel.repositories.CustomerRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
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
    public List<Object[]> getAllCustomers() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Customer> root = query.from(Customer.class);
        Join<Customer, User> join = root.join("user", JoinType.LEFT);
        query.multiselect(root.get("id"), root.get("name"),
                root.get("email"), root.get("phone"),
                root.get("address"), root.get("active"));

        query.where(builder.or(builder.equal(join.get("role"), RoleUser.ROLE_CUSTOMER.name()),
                builder.isNull(root.get("user"))));

        return session.createQuery(query).getResultList();
    }
}
