package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO extends BaseDTO {
    private String roomNumber;
    private Integer floor;
    private RoomTypeDTO type;
    private String status;
    private String availabilityStatus;
}