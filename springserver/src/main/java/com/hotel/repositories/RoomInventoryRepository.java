package com.hotel.repositories;

import com.hotel.entity.RoomInventory;

import java.time.LocalDate;
import java.util.List;

public interface RoomInventoryRepository extends BaseRepository<RoomInventory> {
    void addOrUpdate(RoomInventory roomInventory, LocalDate inventoryDate);
}
