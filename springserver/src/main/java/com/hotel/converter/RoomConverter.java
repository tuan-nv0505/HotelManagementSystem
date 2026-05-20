package com.hotel.converter;

import com.hotel.dto.RoomDTO;
import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.Room;
import com.hotel.entity.RoomType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Room toRoomEntity(RoomDTO roomDTO) {
        return this.modelMapper.map(roomDTO, Room.class);
    }

    public RoomDTO toRoomDTO(Room roomEntity) {
        return this.modelMapper.map(roomEntity, RoomDTO.class);
    }

    public List<RoomDTO> toRoomDTO(List<Room> listRoomEntity) {
        return listRoomEntity.stream().map(
                element -> this.modelMapper.map(element, RoomDTO.class)
        ).collect(Collectors.toList());
    }
}
