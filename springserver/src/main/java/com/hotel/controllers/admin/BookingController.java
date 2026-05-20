package com.hotel.controllers.admin;

import com.hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public String bookingView(Model model) {
        model.addAttribute("listBookings", bookingService.getAllBookings());
        return "booking";
    }
}
