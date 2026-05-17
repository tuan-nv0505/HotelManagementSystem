package com.hotel.repositories;

import com.hotel.entity.User;

public interface UserRepository {
    User getUserByUsername(String username);
}
