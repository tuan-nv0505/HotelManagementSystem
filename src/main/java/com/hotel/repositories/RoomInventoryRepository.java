package com.hotel.repositories;

import com.hotel.entity.RoomInventory;

import java.time.LocalDate;
import java.util.List;

public interface RoomInventoryRepository {
    void updateRoomInventory(int roomTypeId, LocalDate inventoryDate);
    List<RoomInventory> getListRoomInventory(Integer roomTypeId, LocalDate inventoryDate);
}
