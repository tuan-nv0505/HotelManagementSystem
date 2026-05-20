package com.hotel.services.impl;

import com.hotel.converter.CustomerConverter;
import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.repositories.CustomerRepository;
import com.hotel.repositories.UserRepository;
import com.hotel.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CustomerDTO> listCustomer(Map<String, String> params) {
        List<Customer> customerList = customerRepository.listCustomer(params);
        List<CustomerDTO> listCustomer = new ArrayList<>();
        for (Customer c : customerList) {
            CustomerDTO customerDTO = customerConverter.toCustomerDTO(c);
            listCustomer.add(customerDTO);
        }
        return listCustomer;
    }

    @Override
    public long countCustomer(Map<String, String> params) {
        return customerRepository.countCustomer(params);
    }

    @Override
    public void addOrUpdateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerConverter.toCustomer(customerDTO);

        if (customerDTO.getUserId() != null) {
            User user = userRepository.getUserById(customerDTO.getUserId());
            customer.setUser(user);
        } else {
            customer.setUser(null);
        }
        this.customerRepository.addOrUpdateCustomer(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        this.customerRepository.deleteCustomer(id);
    }

    @Override
    public void deleteCustomer(List<Integer> ids) {
        this.customerRepository.deleteCustomer(ids);
    }
}
