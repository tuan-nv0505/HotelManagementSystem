package com.hotel.repository;

import java.time.LocalDate;

public interface RoomInventoryRepository {
    void updateRoomInventory(int roomTypeId, LocalDate inventoryDate);
}
