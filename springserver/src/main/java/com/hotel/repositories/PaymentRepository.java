package com.hotel.repositories;

import com.hotel.entity.Payment;
import com.hotel.entity.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentRepository {
    List<Payment> listPayment(Map<String, String> params);

    long countPayment(Map<String, String> params);
}
