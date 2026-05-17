package com.hotel.services;

import com.hotel.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<User> getAllUsers();
}
