package com.hotel.repositories.impl;

import com.hotel.entity.*;
import com.hotel.repositories.RoomInventoryRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RoomInventoryRepositoryImpl implements RoomInventoryRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addOrUpdate(RoomInventory roomInventory, LocalDate inventoryDate) {
        Integer roomTypeId = roomInventory.getRoomType().getId();

        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> totalQuery = builder.createQuery(Long.class);
        Root<Room> rootTotal = totalQuery.from(Room.class);
        totalQuery.select(builder.count(rootTotal))
                .where(builder.and(
                        builder.equal(rootTotal.join("type").get("id"), roomTypeId),
                        builder.isTrue(rootTotal.get("active"))
                ));
        Long totalCount = session.createQuery(totalQuery).uniqueResult();
        int totalRooms = totalCount != null ? totalCount.intValue() : 0;

        CriteriaQuery<Long> maintenanceQuery = builder.createQuery(Long.class);
        Root<Room> rootMaintenance = maintenanceQuery.from(Room.class);
        maintenanceQuery.select(builder.count(rootMaintenance.get("id")))
                .where(builder.and(
                        builder.equal(rootMaintenance.get("type").get("id"), roomTypeId),
                        builder.isTrue(rootMaintenance.get("active")),
                        builder.equal(rootMaintenance.get("availabilityStatus"), "MAINTENANCE")
                ));
        Long maintenanceCount = session.createQuery(maintenanceQuery).uniqueResult();
        int maintenanceRooms = maintenanceCount != null ? maintenanceCount.intValue() : 0;

        CriteriaQuery<Long> reservedQuery = builder.createQuery(Long.class);
        Root<BookingRoom> rootReserved = reservedQuery.from(BookingRoom.class);
        Join<BookingRoom, Booking> bookingJoin = rootReserved.join("booking", JoinType.INNER);
        Join<BookingRoom, Room> roomJoin = rootReserved.join("room", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(roomJoin.get("type").get("id"), roomTypeId));
        predicates.add(builder.lessThanOrEqualTo(bookingJoin.get("expectedCheckIn"), inventoryDate));
        predicates.add(builder.greaterThan(bookingJoin.get("expectedCheckOut"), inventoryDate));
        predicates.add(builder.notEqual(bookingJoin.get("status"), "CANCELLED"));
        predicates.add(builder.notEqual(bookingJoin.get("status"), "CHECKED_OUT"));

        reservedQuery.select(builder.countDistinct(roomJoin.get("id")));
        reservedQuery.where(predicates.toArray(Predicate[]::new));

        Long reservedCount = session.createQuery(reservedQuery).uniqueResult();
        int reservedRooms = reservedCount != null ? reservedCount.intValue() : 0;

        int availableRooms = totalRooms - maintenanceRooms - reservedRooms;

        CriteriaQuery<RoomInventory> invQuery = builder.createQuery(RoomInventory.class);
        Root<RoomInventory> rootInv = invQuery.from(RoomInventory.class);
        invQuery.where(builder.and(
                builder.equal(rootInv.get("roomType").get("id"), roomTypeId),
                builder.equal(rootInv.get("inventoryDate"), inventoryDate)
        ));
        RoomInventory inventory = session.createQuery(invQuery).uniqueResult();

        if (inventory == null) {
            inventory = new RoomInventory();
            RoomType rt = session.load(RoomType.class, roomTypeId);
            inventory.setRoomType(rt);
            inventory.setInventoryDate(inventoryDate);
        }
        inventory.setTotalRooms(totalRooms);
        inventory.setMaintenanceRooms(maintenanceRooms);
        inventory.setReservedRooms(reservedRooms);
        inventory.setAvailableRooms(availableRooms);

        session.saveOrUpdate(inventory);
    }


    @Override
    public List<Predicate> getPredicates(Map<String, String> params, CriteriaBuilder builder, Root<RoomInventory> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String roomTypeId = params.get("roomTypeId");
            if (roomTypeId != null) {
                predicates.add(builder.equal(root.get("roomType").get("id"), roomTypeId));
            }

            String inventoryDate = params.get("inventoryDate");
            if (inventoryDate != null) {
                predicates.add(builder.equal(root.get("inventoryDate"), inventoryDate));
            }
        }

        return predicates;
    }

    @Override
    public List<RoomInventory> list(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RoomInventory> criteriaQuery = builder.createQuery(RoomInventory.class);
        Root<RoomInventory> root = criteriaQuery.from(RoomInventory.class);

        root.fetch("roomType");
        criteriaQuery.select(root);

        criteriaQuery.where(this.getPredicates(params, builder, root).toArray(Predicate[]::new));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public long count(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<RoomInventory> root = criteriaQuery.from(RoomInventory.class);criteriaQuery.select(builder.count(root));

        List<Predicate> predicates = getPredicates(params, builder, root);
        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        Query query = session.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    @Override
    public void addOrUpdate(RoomInventory dto) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void delete(List<Integer> ids) {

    }

    @Override
    public RoomInventory get(int id) {
        return null;
    }

    @Override
    public RoomInventory save(RoomInventory entity) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(entity);
        return entity;
    }
}
