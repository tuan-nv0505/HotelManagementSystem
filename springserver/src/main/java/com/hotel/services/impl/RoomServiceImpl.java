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
}
