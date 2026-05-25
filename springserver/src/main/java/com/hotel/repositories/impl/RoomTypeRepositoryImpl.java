package com.hotel.repositories.impl;

import com.hotel.entity.RoomInventory;
import com.hotel.entity.RoomType;
import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomTypeRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RoomTypeRepositoryImpl implements RoomTypeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<RoomType> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RoomType> criteriaQuery = builder.createQuery(RoomType.class);
        Root<RoomType> root = criteriaQuery.from(RoomType.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(root.get("id")));

        Query query = session.createQuery(criteriaQuery);

        if (params != null) {
            int pageSize = this.env.getProperty("room_types.page_size", Integer.class);
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
        Root<RoomType> root = criteriaQuery.from(RoomType.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<RoomType> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), Double.parseDouble(toPrice)));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(RoomType RoomType) {
        Session session = this.factory.getObject().getCurrentSession();
        if (RoomType.getId() == null) {
            session.persist(RoomType);
        } else {
            session.merge(RoomType);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        RoomType RoomType = session.get(RoomType.class, id);
        session.remove(RoomType);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<RoomType> criteriaDelete = builder.createCriteriaDelete(RoomType.class);
        Root<RoomType> root = criteriaDelete.from(RoomType.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public RoomType get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(RoomType.class, id);
    }

    @Override
    public RoomType save(RoomType entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
