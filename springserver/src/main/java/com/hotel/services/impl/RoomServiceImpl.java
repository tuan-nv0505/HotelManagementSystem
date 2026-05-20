package com.hotel.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.converter.RoomConverter;
import com.hotel.dto.RoomDTO;
import com.hotel.repositories.RoomRepository;
import com.hotel.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomConverter roomConverter;

    @Override
    public List<RoomDTO> listRoom(Map<String, String> params) {
        return this.roomConverter.toRoomDTO(this.roomRepository.listRoom(params));
    }

    @Override
    public long countRoom(Map<String, String> params) {
        return this.roomRepository.countRoom(params);
    }

    @Override
    public void addOrUpdateRoom(RoomDTO roomDTO) {
        this.roomRepository.addOrUpdateRoom(this.roomConverter.toRoomEntity(roomDTO));
    }

    @Override
    public void deleteRoom(int id) {
        this.roomRepository.deleteRoom(id);
    }

    @Override
    public void deleteRoom(List<Integer> ids) {
        this.roomRepository.deleteRoom(ids);
    }
}
