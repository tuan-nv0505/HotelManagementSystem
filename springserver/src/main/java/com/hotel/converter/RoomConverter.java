package com.hotel.converter;

import com.hotel.dto.RoomDTO;
import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.Room;
import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomRepository;
import com.hotel.repositories.RoomTypeRepository;
import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public Room toRoomEntity(RoomDTO roomDTO) {
        Room room = this.modelMapper.map(roomDTO, Room.class);
        room.setType(this.roomTypeRepository.get(roomDTO.getTypeId()));
        return room;
    }

    public RoomDTO toRoomDTO(Room roomEntity) {
        RoomDTO roomDTO = this.modelMapper.map(roomEntity, RoomDTO.class);
        roomDTO.setTypeId(roomEntity.getType().getId());
        roomDTO.setTypeName(roomEntity.getType().getName());
        return roomDTO;
    }

    public List<RoomDTO> toRoomDTO(List<Room> listRoomEntity) {
        return listRoomEntity.stream().map(
                element -> this.toRoomDTO(element)
        ).collect(Collectors.toList());
    }
}
