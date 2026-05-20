package com.hotel.services;

import com.hotel.dto.UserDTO;
import com.hotel.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    List<UserDTO> listUser(Map<String, String> params);

    long countUser(Map<String, String> params);

    void addOrUpdateUser(UserDTO UserDTO);

    void deleteUser(int id);

    void deleteUser(List<Integer> ids);
}
