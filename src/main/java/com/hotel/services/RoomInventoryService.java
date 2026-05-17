package com.hotel.services;

import com.hotel.entity.RoomInventory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface RoomInventoryService {
    List<RoomInventory> getListRoomInventory(Integer roomTypeId, LocalDate inventoryDate);
}
