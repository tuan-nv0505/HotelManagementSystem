package com.hotel.repositories.impl;

import com.hotel.entity.BookingService;
import com.hotel.entity.RoomInventory;
import com.hotel.repositories.BookingServiceRepository;
import com.hotel.repositories.BookingServiceRepository;
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
public class BookingServiceRepositoryImpl implements BookingServiceRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<BookingService> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BookingService> criteriaQuery = builder.createQuery(BookingService.class);
        Root<BookingService> root = criteriaQuery.from(BookingService.class);
        root.fetch("service");
        criteriaQuery.select(root);

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(root.get("id")));

        Query query = session.createQuery(criteriaQuery);

        if (params != null) {
            int pageSize = this.env.getProperty("booking_services.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "0"));
            int start = page * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public long count(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<BookingService> root = criteriaQuery.from(BookingService.class);criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<BookingService> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String bookingId = params.get("bookingId");
            if (bookingId != null && !bookingId.isEmpty()) {
                predicates.add(builder.equal(root.get("booking").get("id"), bookingId));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(BookingService BookingService) {
        Session session = this.factory.getObject().getCurrentSession();
        if (BookingService.getId() == null) {
            session.persist(BookingService);
        } else {
            session.merge(BookingService);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        BookingService bookingService = session.get(BookingService.class, id);
        session.remove(bookingService);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<BookingService> criteriaDelete = builder.createCriteriaDelete(BookingService.class);
        Root<BookingService> root = criteriaDelete.from(BookingService.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public BookingService get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(BookingService.class, id);
    }

    @Override
    public boolean existsByService(int serviceId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
        Root<BookingService> root = criteriaQuery.from(BookingService.class);
        criteriaQuery.select(root.get("id"));
        criteriaQuery.where(builder.equal(root.get("service").get("id"), serviceId));

        Query query = session.createQuery(criteriaQuery);
        query.setMaxResults(1);

        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean existsByService(List<Integer> listServiceId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
        Root<BookingService> root = criteriaQuery.from(BookingService.class);

        criteriaQuery.select(root.get("id"));
        criteriaQuery.where(root.get("service").get("id").in(listServiceId));

        Query query = session.createQuery(criteriaQuery);
        query.setMaxResults(1);

        return !query.getResultList().isEmpty();
    }

    @Override
    public BookingService save(BookingService entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
