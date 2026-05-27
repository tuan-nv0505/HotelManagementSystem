package com.hotel.services;

import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.Service;

import java.util.List;
import java.util.Map;

public interface CustomerService extends BaseService<CustomerDTO> {
    Customer getOrAdd(String name, String email, String phone, Integer userId);

}
