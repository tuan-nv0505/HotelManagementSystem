package com.hotel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room_type")
@Getter
@Setter
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "base_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @ColumnDefault("2")
    @Column(name = "capacity")
    private Integer capacity;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ColumnDefault("1")
    @Column(name = "active")
    private Boolean active = true;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<RoomInventory> roomInventories;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Room> rooms;
}