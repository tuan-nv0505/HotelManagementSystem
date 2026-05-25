package com.hotel.services;

import com.hotel.dto.RoomDTO;
import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Room;

import java.util.List;
import java.util.Map;

public interface RoomService extends BaseService<RoomDTO> {
    List<RoomDTO> findAvailableRooms(Map<String, String> params);
    long countAvailableRoom(Map<String, String> params);
}
