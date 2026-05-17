package com.hotel.repositories;

import com.hotel.entity.Room;

import java.util.List;
import java.util.Map;

public interface RoomRepository {
    List<Room> getRooms(Map<String, Object> params);
}
