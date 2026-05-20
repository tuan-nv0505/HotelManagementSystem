package com.hotel.converter;

import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.RoomType;
import com.hotel.services.RoomTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomTypeConverter {
    @Autowired
    private ModelMapper modelMapper;

    public RoomType toRoomTypeEntity(RoomTypeDTO roomTypeDTO) {
        return this.modelMapper.map(roomTypeDTO, RoomType.class);
    }

    public RoomTypeDTO toRoomTypeDTO(RoomType roomTypeEntity) {
        return this.modelMapper.map(roomTypeEntity, RoomTypeDTO.class);
    }

    public List<RoomTypeDTO> toRoomTypeDTO(List<RoomType> listRoomTypeEntity) {
        return listRoomTypeEntity.stream().map(
                element -> this.modelMapper.map(element, RoomTypeDTO.class)
        ).collect(Collectors.toList());
    }
}
