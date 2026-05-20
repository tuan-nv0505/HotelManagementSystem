package com.hotel.converter;

import com.hotel.dto.UserDTO;
import com.hotel.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
