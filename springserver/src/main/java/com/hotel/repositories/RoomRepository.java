package com.hotel.repositories;

import com.hotel.entity.Room;
import com.hotel.entity.Room;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomRepository extends BaseRepository<Room>{
    List<Room> findAvailableRooms(Map<String, String> params);
    long countAvailableRoom(Map<String, String> params);
}
