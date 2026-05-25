package com.hotel.repositories.impl;

import com.hotel.entity.Booking;
import com.hotel.entity.Customer;
import com.hotel.entity.RoomInventory;
import com.hotel.repositories.BookingRepository;
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
public class BookingRepositoryImpl implements BookingRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<Booking> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Booking> query = builder.createQuery(Booking.class);
        Root<Booking> root = query.from(Booking.class);
        query.select(root);
        query.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);

        if (params != null) {
            int pageSize = this.env.getProperty("bookings.page_size", Integer.class);
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
        Root<Booking> root = criteriaQuery.from(Booking.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Booking> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String customerName = params.get("customerName");
            if (customerName != null && !customerName.isEmpty()) {
                Join<Booking, Customer> joinCustomer = root.join("customer", JoinType.INNER);
                predicates.add(builder.like(joinCustomer.get("name"), String.format("%%%s%%", customerName)));
            }
            String fromDate = params.get("fromDate");
            if (fromDate != null && !fromDate.isEmpty()) {
                java.sql.Date sqlFromDate = java.sql.Date.valueOf(fromDate);
                predicates.add(builder.greaterThanOrEqualTo(root.get("expectedCheckIn"), sqlFromDate));
            }
            String toDate = params.get("toDate");
            if (toDate != null && !toDate.isEmpty()) {
                java.sql.Date sqlToDate = java.sql.Date.valueOf(toDate);
                predicates.add(builder.lessThanOrEqualTo(root.get("expectedCheckOut"), sqlToDate));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(Booking booking) {
        Session session = this.factory.getObject().getCurrentSession();
        if (booking.getId() == null) {
            session.persist(booking);
        } else {
            session.merge(booking);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Booking booking = session.get(Booking.class, id);
        session.remove(booking);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Booking> criteriaDelete = builder.createCriteriaDelete(Booking.class);
        Root<Booking> root = criteriaDelete.from(Booking.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Booking get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Booking.class, id);
    }

    @Override
    public void flush() {
        Session session = this.factory.getObject().getCurrentSession();
        session.flush();
    }

    @Override
    public Booking save(Booking entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
