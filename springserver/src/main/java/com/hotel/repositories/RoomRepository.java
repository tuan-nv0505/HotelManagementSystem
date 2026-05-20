package com.hotel.repositories;

import com.hotel.entity.Room;
import com.hotel.entity.Room;

import java.util.List;
import java.util.Map;

public interface RoomRepository {
    List<Room> listRoom(Map<String, String> params);
    long countRoom(Map<String, String> params);
    void addOrUpdateRoom(Room room);
    void deleteRoom(int id);
    void deleteRoom(List<Integer> ids);
}
