package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean active;
}