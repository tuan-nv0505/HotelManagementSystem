package com.hotel.dto.requestbooking;

import lombok.Data;

@Data
public class DetailRoomDTO {
    private Long id;
    private String roomNumber;
    private Double price;
    private Long roomTypeId;
    private String roomTypeName;

    @Override
    public String toString() {
        return "RoomBookingDTO{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", roomTypeId=" + roomTypeId +
                ", roomTypeName='" + roomTypeName + '\'' +
                '}';
    }
}
