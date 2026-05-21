package com.hotel.dto;

import com.hotel.enums.AvailabilityStatus;
import com.hotel.enums.StatusRoom;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomDTO extends BaseDTO {
    private String roomNumber;
    private Integer floor;
    private Integer typeId;
    private String typeName;
    private BigDecimal typeBasePrice;
    private StatusRoom status;
    private AvailabilityStatus availabilityStatus;
}