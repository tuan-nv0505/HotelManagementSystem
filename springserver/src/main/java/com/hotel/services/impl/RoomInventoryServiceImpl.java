package com.hotel.services.impl;

import com.hotel.converter.RoomInventoryConverter;
import com.hotel.dto.RoomInventoryDTO;
import com.hotel.entity.RoomInventory;
import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomInventoryRepository;
import com.hotel.repositories.RoomRepository;
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
    @Autowired
    private RoomInventoryConverter roomInventoryConverter;

    @Override
    public void addOrUpdate(RoomInventoryDTO roomInventoryDTO, LocalDate inventoryDate) {
        RoomType roomType = new RoomType();
        roomType.setId(roomInventoryDTO.getRoomTypeId());

        RoomInventory roomInventory = this.roomInventoryConverter.toRoomInventory(roomInventoryDTO);
        roomInventory.setRoomType(roomType);

        this.roomInventoryRepository.addOrUpdate(roomInventory, inventoryDate);
    }

    @Override
    public List<RoomInventory> list(Map<String, String> params) {
        return List.of();
    }

    @Override
    public long count(Map<String, String> params) {
        return 0;
    }

    @Override
    public void addOrUpdate(RoomInventory entity) {

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
        return null;
    }
}
