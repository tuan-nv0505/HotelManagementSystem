package com.hotel.repositories.impl;

import com.hotel.entity.Payment;
import com.hotel.entity.Payment;
import com.hotel.entity.User;
import com.hotel.repositories.PaymentRepository;
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
public class PaymentRepositoryImpl implements PaymentRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<Payment> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Payment> query = builder.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);
        query.select(root);
        query.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));
        Query q = session.createQuery(query);

        if (params != null) {
            int pageSize = this.env.getProperty("payments.page_size", Integer.class);
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
        Root<Payment> root = criteriaQuery.from(Payment.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }


    private List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Payment> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String transactionCode = params.get("transactionCode");
            if (transactionCode != null && !transactionCode.isEmpty()) {
                predicates.add(builder.like(root.get("transactionCode"), "%" + transactionCode + "%"));
            }

            String staffUsername = params.get("staffUsername");
            if (staffUsername != null && !staffUsername.isEmpty()) {
                Join<Payment, User> staffJoin = root.join("staff", JoinType.LEFT);
                predicates.add(builder.like(staffJoin.get("username"), "%" + staffUsername + "%"));
            }

            String paymentMethod = params.get("paymentMethod");
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                predicates.add(builder.equal(root.get("paymentMethod"), paymentMethod));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(Payment dto) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void delete(List<Integer> ids) {

    }
}
