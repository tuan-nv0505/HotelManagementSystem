package com.hotel.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.converter.RoomConverter;
import com.hotel.dto.RoomDTO;
import com.hotel.dto.RoomInventoryDTO;
import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.Room;
import com.hotel.entity.RoomInventory;
import com.hotel.repositories.RoomRepository;
import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomInventoryService roomInventoryService;

    @Autowired
    private RoomConverter roomConverter;

    @Override
    public List<RoomDTO> list(Map<String, String> params) {
        return this.roomConverter.toRoomDTO(this.roomRepository.list(params));
    }

    @Override
    public long count(Map<String, String> params) {
        return this.roomRepository.count(params);
    }

    @Override
    public void addOrUpdate(RoomDTO roomDTO) {
        this.roomRepository.addOrUpdate(this.roomConverter.toRoomEntity(roomDTO));
        RoomInventoryDTO roomInventoryDTO = new RoomInventoryDTO();
        roomInventoryDTO.setRoomTypeId(roomDTO.getTypeId());
        this.roomInventoryService.addOrUpdate(roomInventoryDTO, Instant.now()
                .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                .toLocalDate());
    }

    @Override
    public void delete(int id) {
        this.roomRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.roomRepository.delete(ids);
    }

    @Override
    public RoomDTO get(int id) {
        return this.roomConverter.toRoomDTO(this.roomRepository.get(id));
    }

    @Override
    public RoomDTO save(RoomDTO entity) {
        return null;
    }

    @Override
    public List<RoomDTO> findAvailableRooms(Map<String, String> params) {
        return this.roomConverter.toRoomDTO(this.roomRepository.findAvailableRooms(params));
    }
}
