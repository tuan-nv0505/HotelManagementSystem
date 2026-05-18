package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Boolean active;
    private Integer userId;
}