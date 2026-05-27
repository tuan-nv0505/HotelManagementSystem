package com.hotel.controllers.apis;

import com.hotel.dto.requestbooking.RequestBookingDTO;
import com.hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIBookingController {
    @Autowired
    private BookingService bookingService;

    @DeleteMapping("/bookings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable(value = "id") int id) {
        this.bookingService.delete(id);
    }

    @DeleteMapping("/bookings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiBooking(@RequestBody List<Map<String, String>> listBookingDelete) {
        List<Integer> ids = new ArrayList<>();
        listBookingDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.bookingService.delete(ids);
    }

    @PostMapping("/secure/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBooking(@RequestBody RequestBookingDTO dto) {
        this.bookingService.processAddBooking(dto);
    }
}
