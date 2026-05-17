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

@Repository
@Transactional
public class RoomInventoryRepositoryImpl implements RoomInventoryRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void updateRoomInventory(int roomTypeId, LocalDate inventoryDate) {
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
        maintenanceQuery.select(builder.count(rootMaintenance))
                .where(builder.and(
                        builder.equal(rootMaintenance.join("type").get("id"), roomTypeId),
                        builder.isTrue(rootMaintenance.get("active")),
                        builder.equal(rootMaintenance.get("availabilityStatus"), "MAINTENANCE")
                ));
        Long maintCount = session.createQuery(maintenanceQuery).uniqueResult();
        int maintenanceRooms = maintCount != null ? maintCount.intValue() : 0;

        CriteriaQuery<Long> reservedQuery = builder.createQuery(Long.class);
        Root<BookingRoom> rootReserved = reservedQuery.from(BookingRoom.class);
        Join<BookingRoom, Booking> bookingJoin = rootReserved.join("booking");
        Join<BookingRoom, Room> roomJoin = rootReserved.join("room");

        Predicate typeCond = builder.equal(roomJoin.join("type").get("id"), roomTypeId);
        Predicate startCond = builder.lessThanOrEqualTo(bookingJoin.get("expectedCheckIn"), inventoryDate);
        Predicate endCond = builder.greaterThan(bookingJoin.get("expectedCheckOut"), inventoryDate);
        Predicate notCancelled = builder.notEqual(bookingJoin.get("status"), "CANCELLED");
        Predicate notCheckedOut = builder.notEqual(bookingJoin.get("status"), "CHECKED_OUT");

        reservedQuery.select(builder.countDistinct(roomJoin.get("id")))
                .where(builder.and(typeCond, startCond, endCond, notCancelled, notCheckedOut));

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
    public List<RoomInventory> getListRoomInventory(Integer roomTypeId, LocalDate inventoryDate) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RoomInventory> criteriaQuery = builder.createQuery(RoomInventory.class);
        Root<RoomInventory> root = criteriaQuery.from(RoomInventory.class);

        root.fetch("roomType");
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (roomTypeId != null) {
            predicates.add(builder.equal(root.get("roomType").get("id"), roomTypeId));
        }
        if (inventoryDate != null) {
            predicates.add(builder.equal(root.get("inventoryDate"), inventoryDate));
        }
        System.out.println(inventoryDate);

        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        return session.createQuery(criteriaQuery).getResultList();
    }
}
