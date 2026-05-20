package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDTO {
    private Integer id;
    private Boolean active;
}
