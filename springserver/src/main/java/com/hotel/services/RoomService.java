package com.hotel.services;

import com.hotel.dto.RoomDTO;
import com.hotel.dto.ServiceDTO;

import java.util.List;
import java.util.Map;

public interface RoomService extends BaseService<RoomDTO> {
    RoomDTO get(int id);
}
