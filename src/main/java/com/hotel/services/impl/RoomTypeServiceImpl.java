package com.hotel.services.impl;

import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomTypeRepository;
import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public List<RoomType> listRoomType() {
        return this.roomTypeRepository.listRoomType();
    }
}
