package com.hotel.repositories;

import com.hotel.entity.User;
import com.hotel.entity.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    User getUserByUsername(String username);

    User getUserById(Integer id);

    List<User> listUser(Map<String, String> params);

    long countUser(Map<String, String> params);

    void addOrUpdateUser(User user);

    void deleteUser(int id);

    void deleteUser(List<Integer> ids);
}
