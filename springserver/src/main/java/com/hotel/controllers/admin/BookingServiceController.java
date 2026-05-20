package com.hotel.controllers.admin;


import com.hotel.dto.BookingServiceDTO;
import com.hotel.enums.AvailabilityStatus;
import com.hotel.services.BookingServiceService;
import com.hotel.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class BookingServiceController {
    @Autowired
    private BookingServiceService bookingServiceService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private Environment env;

    @GetMapping("/booking-services")
    public String BookingServiceView(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("bookingServiceDTO", new BookingServiceDTO());
        model.addAttribute("listBookingService", this.bookingServiceService.list(params));
        model.addAttribute("listService", this.serviceService.list(null));

        model.addAttribute("bookingId", params.get("bookingId"));

        int pageSize = this.env.getProperty("booking_services.page_size", Integer.class, 5);
        long totalBookingServices = this.bookingServiceService.count(params);
        int totalPages = (int) Math.ceil((double) totalBookingServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "booking_service";
    }

    @PostMapping("/booking-services")
    public String processBookingService(@ModelAttribute(name = "bookingServiceDTO") BookingServiceDTO bookingServiceDTO) {
        this.bookingServiceService.addOrUpdate(bookingServiceDTO);
        return "redirect:/admin/booking-services";
    }
}
