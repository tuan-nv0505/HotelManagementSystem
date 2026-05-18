package com.hotel.controllers.admin;

import com.hotel.entity.Customer;
import com.hotel.entity.Service;
import com.hotel.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String customerView(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("listCustomers", customerService.getAllCustomers());
        return "customer";
    }

    @PostMapping("/customers")
    public String processCustomer(@ModelAttribute(name = "customer") Customer customer) {
        System.out.println(customer.getId());
        System.out.println(customer.getName());
        System.out.println(customer.getActive());
        return "customer";
    }
}
