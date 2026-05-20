package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends BaseDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer userId;
}