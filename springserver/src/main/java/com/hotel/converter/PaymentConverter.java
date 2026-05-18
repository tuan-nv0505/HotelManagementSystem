package com.hotel.converter;

import com.hotel.dto.PaymentDTO;
import com.hotel.entity.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter {
    @Autowired
    private ModelMapper modelMapper;


    public PaymentDTO toPaymentDTO(Payment payment) {
        return modelMapper.map(payment, PaymentDTO.class);
    }

}
