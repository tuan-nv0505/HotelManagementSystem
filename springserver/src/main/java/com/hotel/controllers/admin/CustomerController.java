package com.hotel.controllers.admin;

import com.hotel.dto.CustomerDTO;
import com.hotel.services.CustomerService;
import com.hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private UserService userService;

    @GetMapping("/customers")
    public String customerView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("customer", new CustomerDTO());
        model.addAttribute("listCustomer", this.customerService.list(params));
        model.addAttribute("allUsers", userService.list(null));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("phone", params.get("phone"));

        int pageSize = this.env.getProperty("services.page_size", Integer.class, 5);
        long totalServices = this.customerService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "customer";
    }

    @PostMapping("/customers")
    public String processService(@ModelAttribute(name = "service") CustomerDTO customerDTO, RedirectAttributes redirectAttributes) {
        try {
            customerService.addOrUpdate(customerDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu khách hàng thành công!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/customers";
    }
}
