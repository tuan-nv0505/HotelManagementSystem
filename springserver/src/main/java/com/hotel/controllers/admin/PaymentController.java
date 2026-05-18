package com.hotel.controllers.admin;

import com.hotel.entity.Customer;
import com.hotel.entity.Payment;
import com.hotel.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public String paymentView(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payment";
    }

    @PostMapping("/payments")
    public String processPayment(@ModelAttribute(name = "payment") Payment payment) {
        System.out.println(payment.getId());
        System.out.println(payment.getBooking().getId());
        System.out.println(payment.getStaff().getId());
        return "payment";
    }
}
