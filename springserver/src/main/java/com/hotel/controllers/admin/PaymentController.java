package com.hotel.controllers.admin;

import com.hotel.dto.PaymentDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.Payment;
import com.hotel.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private Environment env;

    @GetMapping("/payments")
    public String paymentView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("payment", new PaymentDTO());
        model.addAttribute("payments", paymentService.list(params));

        model.addAttribute("transactionCode", params.get("transactionCode"));
        model.addAttribute("staffUsername", params.get("staffUsername"));
        model.addAttribute("paymentMethod", params.get("paymentMethod"));

        int pageSize = this.env.getProperty("payments.page_size", Integer.class, 5);
        long totalServices = this.paymentService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "payment";
    }
}
