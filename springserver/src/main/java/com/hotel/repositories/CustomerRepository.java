package com.hotel.repositories;

import com.hotel.entity.Customer;
import com.hotel.entity.User;

public interface CustomerRepository extends BaseRepository<Customer> {
    Customer getCustomerByName(String name);
    Customer getCustomerByUser(User user);
    Customer getOrAdd(String name, String email, String phone, User user);
}
