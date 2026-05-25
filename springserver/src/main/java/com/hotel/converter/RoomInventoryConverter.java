package com.hotel.converter;

import com.hotel.dto.BookingServiceDTO;
import com.hotel.dto.RoomInventoryDTO;
import com.hotel.entity.BookingService;
import com.hotel.entity.RoomInventory;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.RoomInventoryRepository;
import com.hotel.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomInventoryConverter {
    @Autowired
    private ModelMapper modelMapper;

    public RoomInventory toRoomInventory(RoomInventoryDTO roomInventoryDTO) {
        return this.modelMapper.map(roomInventoryDTO, RoomInventory.class);
    }
}
