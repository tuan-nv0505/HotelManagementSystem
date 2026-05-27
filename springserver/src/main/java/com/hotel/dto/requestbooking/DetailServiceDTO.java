package com.hotel.dto.requestbooking;

import lombok.Data;

@Data
public class DetailServiceDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    @Override
    public String toString() {
        return "ServiceBookingDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
