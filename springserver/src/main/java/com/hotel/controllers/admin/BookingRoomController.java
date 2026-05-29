package com.hotel.controllers.admin;

import com.hotel.dto.BookingDTO;
import com.hotel.dto.BookingRoomDTO;
import com.hotel.services.BookingRoomService;
import com.hotel.services.BookingService;
import com.hotel.services.RoomService;
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
public class BookingRoomController {
    @Autowired
    private BookingRoomService bookingRoomService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private Environment env;

    @GetMapping("/booking-rooms")
    public String bookingRoomView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("BookingRoomDTO", new BookingRoomDTO());
        model.addAttribute("allBookings", bookingService.list(null));
        model.addAttribute("allRooms", roomService.list(null));
        model.addAttribute("bookingRooms", bookingRoomService.list(params));

        model.addAttribute("customerName", params.get("customerName"));
        model.addAttribute("roomName", params.get("roomName"));

        int pageSize = this.env.getProperty("booking_rooms.page_size", Integer.class, 5);
        long totalServices = this.bookingRoomService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "booking_room";
    }

    @PostMapping("/booking-rooms")
    public String processBookingRoom(@ModelAttribute(name = "bookingDTO") BookingRoomDTO bookingRoomDTO, RedirectAttributes redirectAttributes) {
        try {
            this.bookingRoomService.addOrUpdate(bookingRoomDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu chi tiết phòng thành công!");

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/booking-rooms";
    }
}
