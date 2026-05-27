package com.hotel.dto.requestbooking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DetailCustomerDTO {
    @NotBlank(message = "Username is not null")
    private String fullName;
    @Email(message = "Email invalid")
    private String email;
    @Pattern(
            regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$",
            message = "Phone invalid"
    )
    private String phone;
    private String address;

    @Override
    public String toString() {
        return "CustomerBookingDTO{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address +
                '}';
    }
}
