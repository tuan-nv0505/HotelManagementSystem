package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomInventoryDTO {
    private Integer id;
    private Integer roomTypeId;
    private String inventoryDate;
    private Integer totalRooms;
    private Integer availableRooms;
    private Integer reservedRooms;
    private Integer maintenanceRooms;
}