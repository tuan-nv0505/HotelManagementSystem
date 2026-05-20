package com.hotel.repositories;

import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.RoomType;

import java.util.List;
import java.util.Map;

public interface RoomTypeRepository {
    List<RoomType> listRoomType(Map<String, String> params);
    long countRoomType(Map<String, String> params);
    void addOrUpdateRoomType(RoomType roomType);
    void deleteRoomType(int id);
    void deleteRoomType(List<Integer> ids);
}
