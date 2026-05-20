package com.hotel.controllers.admin;

import com.hotel.dto.CustomerDTO;
import com.hotel.services.CustomerService;
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
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private Environment env;

    @GetMapping("/customers")
    public String customerView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("customer", new CustomerDTO());
        model.addAttribute("listCustomer", this.customerService.listCustomer(params));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("phone", params.get("phone"));

        int pageSize = this.env.getProperty("services.page_size", Integer.class, 5);
        long totalServices = this.customerService.countCustomer(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "customer";
    }

    @PostMapping("/customers")
    public String processService(@ModelAttribute(name = "service") CustomerDTO customerDTO) {
        this.customerService.addOrUpdateCustomer(customerDTO);
        return "redirect:/admin/customers";
    }
}
