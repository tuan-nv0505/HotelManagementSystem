package com.hotel.services;

import com.hotel.dto.RoomTypeDTO;
import com.hotel.dto.ServiceDTO;
import com.hotel.entity.RoomType;

import java.util.List;
import java.util.Map;

public interface RoomTypeService {
    List<RoomTypeDTO> listRoomType(Map<String, String> params);
    long countRoomType(Map<String, String> params);
    void addOrUpdateRoomType(RoomTypeDTO roomTypeDTO);
    void deleteRoomType(int id);
    void deleteRoomType(List<Integer> ids);
}
