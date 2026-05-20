package com.hotel.controllers.admin;

import com.hotel.dto.BookingDTO;
import com.hotel.dto.CustomerDTO;
import com.hotel.enums.StatusBooking;
import com.hotel.services.BookingService;
import com.hotel.services.CustomerService;
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
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private Environment env;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/bookings")
    public String bookingView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("booking", new BookingDTO());
        model.addAttribute("statusBookings", StatusBooking.values());
        model.addAttribute("allCustomers", customerService.list(null));
        model.addAttribute("listBookings", bookingService.list(params));

        model.addAttribute("customerName", params.get("customerName"));
        model.addAttribute("fromDate", params.get("fromDate"));
        model.addAttribute("toDate", params.get("toDate"));

        int pageSize = this.env.getProperty("bookings.page_size", Integer.class, 5);
        long totalServices = this.bookingService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "booking";
    }

    @PostMapping("/bookings")
    public String processService(@ModelAttribute(name = "booking") BookingDTO bookingDTO, RedirectAttributes redirectAttributes) {
        try {
            this.bookingService.addOrUpdate(bookingDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu đơn đặt phòng thành công!");

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/bookings";
    }
}
