package com.hotel.repositories;

import com.hotel.entity.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Object[]> getAllCustomers();
}
