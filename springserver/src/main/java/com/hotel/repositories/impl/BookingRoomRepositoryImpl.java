package com.hotel.repositories.impl;

import com.hotel.entity.*;
import com.hotel.repositories.BookingRoomRepository;
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
public class BookingRoomRepositoryImpl implements BookingRoomRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<BookingRoom> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BookingRoom> query = builder.createQuery(BookingRoom.class);
        Root<BookingRoom> root = query.from(BookingRoom.class);
        query.select(root);
        query.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);

        if (params != null) {
            int pageSize = this.env.getProperty("booking_rooms.page_size", Integer.class);
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
        Root<BookingRoom> root = criteriaQuery.from(BookingRoom.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<BookingRoom> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String roomName = params.get("roomName");
            if (roomName != null && !roomName.isEmpty()) {
                Join<BookingRoom, Room> join = root.join("room", JoinType.INNER);
                predicates.add(builder.like(join.get("roomNumber"), String.format("%%%s%%", roomName)));
            }
            String customerName = params.get("customerName");
            if (customerName != null && !customerName.trim().isEmpty()) {
                Join<BookingRoom, Booking> bookingJoin = root.join("booking", JoinType.INNER);
                Join<Booking, Customer> customerJoin = bookingJoin.join("customer", JoinType.INNER);
                predicates.add(builder.like(customerJoin.get("name"), String.format("%%%s%%", customerName)));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(BookingRoom BookingRoom) {
        Session session = this.factory.getObject().getCurrentSession();
        if (BookingRoom.getId() == null) {
            session.persist(BookingRoom);
        } else {
            session.merge(BookingRoom);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        BookingRoom BookingRoom = session.get(BookingRoom.class, id);
        session.remove(BookingRoom);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<BookingRoom> criteriaDelete = builder.createCriteriaDelete(BookingRoom.class);
        Root<BookingRoom> root = criteriaDelete.from(BookingRoom.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public BookingRoom get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(BookingRoom.class, id);
    }

    @Override
    public BookingRoom save(BookingRoom entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
