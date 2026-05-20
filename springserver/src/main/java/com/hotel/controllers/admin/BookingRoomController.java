package com.hotel.controllers.admin;

import com.hotel.services.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class BookingRoomController {
    @Autowired
    private BookingRoomService bookingRoomService;
    @Autowired
    private Environment env;

    @GetMapping("/booking_rooms")
    public String bookingRoomView(Model model) {
        model.addAttribute("bookingRooms", bookingRoomService.getAllBookingRooms());
        return "booking_room";
    }
}
