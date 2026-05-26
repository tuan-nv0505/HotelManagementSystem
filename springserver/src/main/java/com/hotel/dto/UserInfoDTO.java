package com.hotel.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserInfoDTO extends BaseDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String roleDisplay;
    private String avatar;
    private String createdAt;
    private MultipartFile file;
    private String name;
    private String address;
}
