package com.hotel.repositories;

import com.hotel.entity.User;
import com.hotel.entity.User;

import java.util.List;
import java.util.Map;

public interface UserRepository extends BaseRepository<User> {
    User getUserByUsername(String username);

    User getUserById(Integer id);

}
