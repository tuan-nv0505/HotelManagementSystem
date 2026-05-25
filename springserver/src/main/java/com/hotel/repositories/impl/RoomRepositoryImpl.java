package com.hotel.repositories.impl;

import com.hotel.entity.Booking;
import com.hotel.entity.BookingRoom;
import com.hotel.entity.Room;
import com.hotel.entity.RoomInventory;
import com.hotel.repositories.RoomRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class RoomRepositoryImpl implements RoomRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<Room> list(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Room> criteriaQuery = builder.createQuery(Room.class);
        Root<Room> root = criteriaQuery.from(Room.class);
        root.fetch("type");
        criteriaQuery.select(root);

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(builder.desc(root.get("id")));

        Query query = session.createQuery(criteriaQuery);

        if (params != null) {
            int pageSize = this.env.getProperty("rooms.page_size", Integer.class);
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
        Root<Room> root = criteriaQuery.from(Room.class);criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<Room> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String floor = params.get("floor");
            if (floor != null && !floor.isEmpty()) {
                predicates.add(builder.equal(root.get("floor"), floor));
            }
        }
        return predicates;
    }

    @Override
    public void addOrUpdate(Room Room) {
        Session session = this.factory.getObject().getCurrentSession();
        if (Room.getId() == null) {
            session.persist(Room);
        } else {
            session.merge(Room);
        }
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Room Room = session.get(Room.class, id);
        session.remove(Room);
    }

    @Override
    public void delete(List<Integer> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Room> criteriaDelete = builder.createCriteriaDelete(Room.class);
        Root<Room> root = criteriaDelete.from(Room.class);

        criteriaDelete.where(root.get("id").in(ids));

        session.createMutationQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Room get(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Room.class, id);
    }

    @Override
    public Room save(Room entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public List<Room> findAvailableRooms(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Room> query = cb.createQuery(Room.class);
        Root<Room> room = query.from(Room.class);
        room.fetch("type");

        Subquery<Integer> bookedRoomSubquery = query.subquery(Integer.class);
        Root<BookingRoom> subBookingRoom = bookedRoomSubquery.from(BookingRoom.class);
        Join<BookingRoom, Booking> subBooking = subBookingRoom.join("booking");

        bookedRoomSubquery.select(subBookingRoom.get("room").get("id"));

        List<Predicate> subPredicates = new ArrayList<>();

        String expectedCheckOutStr = params.get("expectedCheckOut");
        LocalDate expectedCheckOut =
                (expectedCheckOutStr == null || expectedCheckOutStr.isEmpty())
                        ? LocalDate.now()
                        : LocalDate.parse(expectedCheckOutStr);

        String expectedCheckInStr = params.get("expectedCheckIn");
        LocalDate expectedCheckIn =
                (expectedCheckInStr == null || expectedCheckInStr.isEmpty())
                        ? LocalDate.now()
                        : LocalDate.parse(expectedCheckInStr);

        subPredicates.add(cb.lessThan(subBooking.get("expectedCheckIn"), expectedCheckOut));
        subPredicates.add(cb.greaterThan(subBooking.get("expectedCheckOut"), expectedCheckIn));
        subPredicates.add(cb.notEqual(subBooking.get("status"), "CANCELLED"));

        String roomTypeId = params.get("roomTypeId");
        if (roomTypeId != null && !roomTypeId.isEmpty()) {
            Join<BookingRoom, Room> subRoomJoin = subBookingRoom.join("room");
            subPredicates.add(cb.equal(subRoomJoin.get("type").get("id"), roomTypeId));
        }

        bookedRoomSubquery.where(cb.and(subPredicates.toArray(new Predicate[0])));

        List<Predicate> mainPredicates = new ArrayList<>();
        mainPredicates.add(cb.equal(room.get("availabilityStatus"), "READY"));
        mainPredicates.add(cb.equal(room.get("active"), true));
        mainPredicates.add(cb.not(room.get("id").in(bookedRoomSubquery)));

        if (roomTypeId != null && !roomTypeId.isEmpty()) {
            mainPredicates.add(cb.equal(room.get("type").get("id"), roomTypeId));
        }

        query.select(room).where(cb.and(mainPredicates.toArray(new Predicate[0])));
        return session.createQuery(query).getResultList();
    }
}
