package com.hotel.services;

import com.hotel.dto.RoomDTO;

import java.util.List;
import java.util.Map;

public interface RoomService {
    List<RoomDTO> listRoom(Map<String, String> params);
    long countRoom(Map<String, String> params);
    void addOrUpdateRoom(RoomDTO roomDTO);
    void deleteRoom(int id);
    void deleteRoom(List<Integer> ids);
}
