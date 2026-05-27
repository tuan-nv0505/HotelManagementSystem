package com.hotel.repositories.impl;

import com.hotel.entity.RoomInventory;
import com.hotel.entity.Service;
import com.hotel.repositories.ServiceRepository;
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
public class ServiceRepositoryImpl implements ServiceRepository {
    @Autowired
    private Environment env;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Service> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Service> criteriaQuery = builder.createQuery(Service.class);
        Root<Service> root = criteriaQuery.from(Service.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(root.get("id")));

        Query query = session.createQuery(criteriaQuery);

        if (params != null) {
            int pageSize = this.env.getProperty("services.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));

            int start = (page - 1) * pageSize;

            query.setFirstResult(start);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public long count(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Service> root = criteriaQuery.from(Service.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Service> root) {
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
    public void addOrUpdate(Service service) {
        Session session = this.factory.getObject().getCurrentSession();
        if (service.getId() == null) {
            session.persist(service);
        } else {
            session.merge(service);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Service service = session.get(Service.class, id);
        session.remove(service);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Service> criteriaDelete = builder.createCriteriaDelete(Service.class);
        Root<Service> root = criteriaDelete.from(Service.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Service get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Service.class, id);
    }

    @Override
    public Service save(Service entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
