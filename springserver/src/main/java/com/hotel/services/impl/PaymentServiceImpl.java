package com.hotel.services.impl;

import com.hotel.converter.PaymentConverter;
import com.hotel.dto.PaymentDTO;
import com.hotel.entity.Payment;
import com.hotel.repositories.PaymentRepository;
import com.hotel.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentConverter paymentConverter;

    @Override
    public List<PaymentDTO> list(Map<String, String> params) {
        List<Payment> payments = paymentRepository.list(params);

        List<PaymentDTO> paymentResponses = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentDTO paymentRes = paymentConverter.toPaymentDTO(payment);
            paymentRes.setBookingCustomerName(payment.getBooking().getCustomer().getName());
            paymentRes.setStaffUsername(payment.getStaff().getUsername());

            paymentResponses.add(paymentRes);
        }

        return paymentResponses;
    }

    @Override
    public long count(Map<String, String> params) {
        return paymentRepository.count(params);
    }

    @Override
    public void addOrUpdate(PaymentDTO dto) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void delete(List<Integer> ids) {

    }

    @Override
    public PaymentDTO get(int id) {
        return null;
    }

    @Override
    public PaymentDTO save(PaymentDTO entity) {
        return null;
    }
}
