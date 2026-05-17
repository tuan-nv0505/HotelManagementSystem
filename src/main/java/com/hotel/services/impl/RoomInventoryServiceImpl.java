package com.hotel.services.impl;

import com.hotel.entity.RoomInventory;
import com.hotel.repositories.RoomInventoryRepository;
import com.hotel.services.RoomInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RoomInventoryServiceImpl implements RoomInventoryService {
    @Autowired
    private RoomInventoryRepository roomInventoryRepository;

    @Override
    public List<RoomInventory> getListRoomInventory(Integer roomTypeId, LocalDate inventoryDate) {
        return this.roomInventoryRepository.getListRoomInventory(roomTypeId, inventoryDate);
    }
}
