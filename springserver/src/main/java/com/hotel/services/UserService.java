package com.hotel.services;

import com.hotel.dto.UserDTO;
import com.hotel.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService, BaseService<UserDTO> {
}
