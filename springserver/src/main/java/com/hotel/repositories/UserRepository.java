package com.hotel.repositories;

import com.hotel.entity.User;

import java.util.List;

public interface UserRepository {
    User getUserByUsername(String username);

    List<User> getAllUsers();
}
