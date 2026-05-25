package com.hotel.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.converter.RoomTypeConverter;
import com.hotel.dto.RoomTypeDTO;
import com.hotel.repositories.RoomTypeRepository;
import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomTypeConverter roomTypeConverter;

    @Autowired
    private RoomInventoryService roomInventoryService;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<RoomTypeDTO> list(Map<String, String> params) {
        return this.roomTypeConverter.toRoomTypeDTO(this.roomTypeRepository.list(params));
    }


    @Override
    public long count(Map<String, String> params) {
        return this.roomTypeRepository.count(params);
    }


    @Override
    public void addOrUpdate(RoomTypeDTO roomTypeDTO) {
        if (!roomTypeDTO.getFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(roomTypeDTO.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                roomTypeDTO.setImage(res.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.roomTypeRepository.addOrUpdate(this.roomTypeConverter.toRoomTypeEntity(roomTypeDTO));
    }

    @Override
    public void delete(int id) {
        this.roomTypeRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.roomTypeRepository.delete(ids);
    }

    @Override
    public RoomTypeDTO get(int id) {
        return this.roomTypeConverter.toRoomTypeDTO(this.roomTypeRepository.get(id));
    }

    @Override
    public RoomTypeDTO save(RoomTypeDTO entity) {
        return null;
    }
}
