package com.hotel.services.impl;

import com.hotel.entity.Customer;
import com.hotel.repositories.CustomerRepository;
import com.hotel.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public List<Customer> getAllCustomers() {
        List<Object[]> listCustomers = customerRepository.getAllCustomers();

        List<Customer> customers = new ArrayList<>();
        for (Object[] obj : listCustomers) {
            Customer customer = new Customer();
            customer.setId((int) obj[0]);
            customer.setName((String) obj[1]);
            customer.setEmail((String) obj[2]);
            customer.setPhone((String) obj[3]);
            customer.setAddress((String) obj[4]);
            customer.setActive((boolean) obj[5]);

            customers.add(customer);
        }

        return customers;
    }
}
