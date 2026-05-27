package com.hotel.repositories.impl;

import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.exceptions.NotFoundUser;
import com.hotel.repositories.CustomerRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class CustomerRepositoryImpl implements CustomerRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public List<Customer> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root);
        query.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));
        Query q = session.createQuery(query);

        if (params != null) {
            int pageSize = this.env.getProperty("customers.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "0"));
            int start = page * pageSize;

            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Customer> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String phone = params.get("phone");
            if (phone != null && !phone.isEmpty()) {
                predicates.add(builder.like(root.get("phone"), String.format("%%%s%%", phone)));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(Customer customer) {
        Session session = this.factory.getObject().getCurrentSession();
        if (customer.getId() == null) {
            session.persist(customer);
        } else {
            session.merge(customer);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Customer customer = session.get(Customer.class, id);
        session.remove(customer);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Customer> criteriaDelete = builder.createCriteriaDelete(Customer.class);
        Root<Customer> root = criteriaDelete.from(Customer.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Customer get(int id) {
        return null;
    }

    @Override
    public Customer getCustomerByName(String name) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("from Customer where name=:name");
        query.setParameter("name", name);

        return (Customer) query.getSingleResult();
    }

    @Override
    public Customer getCustomerByUser(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        List<Customer> list = session.createQuery(
                "SELECT c FROM Customer c WHERE c.user = :user", Customer.class).setParameter("user", user).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Customer getOrAdd(String name, String email, String phone, User user) {
        Session session = this.factory.getObject().getCurrentSession();;
        String hql = "FROM Customer c WHERE c.email = :email AND c.phone = :phone AND c.user.id = :userId";

        Customer customer = session.createQuery(hql, Customer.class)
                .setParameter("email", email)
                .setParameter("phone", phone)
                .setParameter("userId", user.getId())
                .uniqueResult();

        if (customer != null) {
            return customer;
        }

        customer = new Customer();

        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setUser(user);
        session.persist(customer);

        return customer;
    }

    @Override
    public Customer save(Customer entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
