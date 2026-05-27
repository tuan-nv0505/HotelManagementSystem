package com.hotel.dto.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorDTO {
    private String error;
    private List<String> details;
}
