package com.hotel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private RoomType type;

    @ColumnDefault("'VACANT_CLEAN'")
    @Lob
    @Column(name = "status")
    private String status;

    @ColumnDefault("'READY'")
    @Lob
    @Column(name = "availability_status")
    private String availabilityStatus;

    @ColumnDefault("1")
    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private Set<BookingRoom> bookingRooms;


}