package com.hotel.repositories;

import com.hotel.entity.Customer;
import com.hotel.entity.User;

public interface CustomerRepository extends BaseRepository<Customer> {
    Customer getCustomerByName(String name);

    Customer getCustomerByUser(User user);
}
