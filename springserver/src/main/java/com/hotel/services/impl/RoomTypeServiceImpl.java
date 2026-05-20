package com.hotel.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.converter.RoomTypeConverter;
import com.hotel.dto.RoomTypeDTO;
import com.hotel.entity.RoomType;
import com.hotel.repositories.RoomTypeRepository;
import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomTypeConverter roomTypeConverter;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<RoomTypeDTO> listRoomType(Map<String, String> params) {
        return this.roomTypeConverter.toRoomTypeDTO(this.roomTypeRepository.listRoomType(params));
    }


    @Override
    public long countRoomType(Map<String, String> params) {
        return this.roomTypeRepository.countRoomType(params);
    }

    @Override
    public void addOrUpdateRoomType(RoomTypeDTO roomTypeDTO) {
        if (!roomTypeDTO.getFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(roomTypeDTO.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                roomTypeDTO.setImage(res.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.roomTypeRepository.addOrUpdateRoomType(this.roomTypeConverter.toRoomTypeEntity(roomTypeDTO));
    }

    @Override
    public void deleteRoomType(int id) {
        this.roomTypeRepository.deleteRoomType(id);
    }

    @Override
    public void deleteRoomType(List<Integer> ids) {
        this.roomTypeRepository.deleteRoomType(ids);
    }
}
