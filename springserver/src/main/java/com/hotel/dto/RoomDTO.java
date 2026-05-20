package com.hotel.dto;

import com.hotel.enums.AvailabilityStatus;
import com.hotel.enums.StatusRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO extends BaseDTO {
    private String roomNumber;
    private Integer floor;
    private Integer typeId;
    private String typeName;
    private StatusRoom status;
    private AvailabilityStatus availabilityStatus;
}