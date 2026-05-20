package com.hotel.repositories;

import com.hotel.entity.Customer;

public interface CustomerRepository extends BaseRepository<Customer> {
    Customer getCustomerByName(String name);
}
