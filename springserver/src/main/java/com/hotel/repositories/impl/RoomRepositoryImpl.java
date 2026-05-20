package com.hotel.repositories.impl;

import com.hotel.entity.Room;
import com.hotel.repositories.RoomRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RoomRepositoryImpl implements RoomRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<Room> listRoom(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Room> criteriaQuery = builder.createQuery(Room.class);
        Root<Room> root = criteriaQuery.from(Room.class);

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(root.get("id")));

        Query query = session.createQuery(criteriaQuery);

        if (params != null) {
            int pageSize = this.env.getProperty("room.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "0"));
            int start = page * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public long countRoom(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Room> root = criteriaQuery.from(Room.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    private List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Room> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdateRoom(Room Room) {
        Session session = this.factory.getObject().getCurrentSession();
        if (Room.getId() == null) {
            session.persist(Room);
        } else {
            session.merge(Room);
        }
    }

    @Override
    public void deleteRoom(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Room Room = session.get(Room.class, id);
        session.remove(Room);
    }

    @Override
    public void deleteRoom(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Room> criteriaDelete = builder.createCriteriaDelete(Room.class);
        Root<Room> root = criteriaDelete.from(Room.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }
}
