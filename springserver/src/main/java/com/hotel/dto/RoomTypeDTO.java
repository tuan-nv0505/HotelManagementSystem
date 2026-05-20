package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomTypeDTO {

    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private Integer capacity;
    private String description;
    private MultipartFile file;
    private String image;
    private Boolean active;
}