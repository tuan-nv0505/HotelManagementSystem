package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String avatar;
    private Boolean active;
    private String createdAt;
}