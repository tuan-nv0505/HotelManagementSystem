package com.hotel.dto.requestbooking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RequestBookingDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expectedCheckIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expectedCheckOut;
    private List<DetailRoomDTO> rooms;
    private List<DetailServiceDTO> services;
    private DetailCustomerDTO customer;
    private BigDecimal totalPrice;

    @Override
    public String toString() {
        return "BookingRequestDTO{" +
                "expectedCheckIn='" + expectedCheckIn.toString() + '\'' +
                ", expectedCheckOut='" + expectedCheckOut.toString() + '\'' +
                ", rooms=" + rooms +
                ", services=" + services +
                ", totalPrice" + totalPrice +
                ", customer=" + customer +
                '}';
    }
}
