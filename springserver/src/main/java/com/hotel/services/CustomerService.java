package com.hotel.services;

import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<CustomerDTO> listCustomer(Map<String, String> params);

    long countCustomer(Map<String, String> params);

    void addOrUpdateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(int id);

    void deleteCustomer(List<Integer> ids);
}
