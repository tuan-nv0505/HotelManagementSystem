package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String roleDisplay;
    private String avatar;
    private String createdAt;
    private MultipartFile file;
}