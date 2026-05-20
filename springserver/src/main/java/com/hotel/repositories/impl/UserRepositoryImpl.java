package com.hotel.repositories.impl;

import com.hotel.entity.User;
import com.hotel.entity.User;
import com.hotel.repositories.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public User getUserByUsername(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);

        return (User) query.getSingleResult();
    }

    @Override
    public User getUserById(Integer id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", id);

        return (User) query.getSingleResult();
    }

    @Override
    public List<User> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));
        Query q = session.createQuery(query);

        if (params != null) {
            int pageSize = this.env.getProperty("users.page_size", Integer.class);
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
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    private List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<User> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("username"), String.format("%%%s%%", kw)));
            }

            String phone = params.get("phone");
            if (phone != null && !phone.isEmpty()) {
                predicates.add(builder.like(root.get("phone"), String.format("%%%s%%", phone)));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
        } else {
            session.merge(user);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        User User = session.get(User.class, id);
        session.remove(User);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<User> criteriaDelete = builder.createCriteriaDelete(User.class);
        Root<User> root = criteriaDelete.from(User.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }
}
