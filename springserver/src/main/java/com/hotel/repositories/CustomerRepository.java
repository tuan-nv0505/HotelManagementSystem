package com.hotel.repositories;

import com.hotel.entity.Customer;
import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface CustomerRepository {
    List<Customer> listCustomer(Map<String, String> params);

    long countCustomer(Map<String, String> params);

    void addOrUpdateCustomer(Customer customer);

    void deleteCustomer(int id);

    void deleteCustomer(List<Integer> ids);
}
