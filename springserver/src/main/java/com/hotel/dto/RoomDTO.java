package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {

    private Integer id;
    private String roomNumber;
    private Integer floor;
    private Integer typeId;
    private String status;
    private String availabilityStatus;
    private Boolean active;

}