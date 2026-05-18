package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomTypeDTO {

    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private Integer capacity;
    private String description;
    private String image;
    private Boolean active;
}