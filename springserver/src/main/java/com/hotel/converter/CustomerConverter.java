package com.hotel.converter;

import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Customer toCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

}
