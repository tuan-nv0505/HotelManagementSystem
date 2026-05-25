package com.hotel.services.impl;

import com.hotel.converter.CustomerConverter;
import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.repositories.CustomerRepository;
import com.hotel.repositories.UserRepository;
import com.hotel.services.CustomerService;
import jakarta.persistence.NoResultException;
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
    public List<CustomerDTO> list(Map<String, String> params) {
        List<Customer> customerList = customerRepository.list(params);
        List<CustomerDTO> listCustomer = new ArrayList<>();
        for (Customer c : customerList) {
            CustomerDTO customerDTO = customerConverter.toCustomerDTO(c);
            if (c.getUser() != null) {
                customerDTO.setUserName(c.getUser().getUsername());
            } else {
                customerDTO.setUserName("");
            }

            listCustomer.add(customerDTO);
        }
        return listCustomer;
    }

    @Override
    public long count(Map<String, String> params) {
        return customerRepository.count(params);
    }

    @Override
    public void addOrUpdate(CustomerDTO customerDTO) {
        Customer customer = customerConverter.toCustomer(customerDTO);

        if (customerDTO.getUserName() != null && !customerDTO.getUserName().trim().isEmpty()) {
            try {
                User user = userRepository.getUserByUsername(customerDTO.getUserName());
                customer.setUser(user);
            } catch (NoResultException ex) {
                throw new RuntimeException("Tài khoản User '" + customerDTO.getUserName() + "' không tồn tại. Vui lòng kiểm tra lại!");
            }
        } else {
            customer.setUser(null);
        }
        this.customerRepository.addOrUpdate(customer);
    }

    @Override
    public void delete(int id) {
        this.customerRepository.delete(id);
    }

    @Override
    public void delete(List<Integer> ids) {
        this.customerRepository.delete(ids);
    }

    @Override
    public CustomerDTO get(int id) {
        return null;
    }

    @Override
    public CustomerDTO save(CustomerDTO entity) {
        return null;
    }

}
