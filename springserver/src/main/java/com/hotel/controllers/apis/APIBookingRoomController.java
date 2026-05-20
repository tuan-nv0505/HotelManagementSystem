package com.hotel.controllers.apis;

import com.hotel.services.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIBookingRoomController {
    @Autowired
    private BookingRoomService bookingRoomService;

    @DeleteMapping("/booking_rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable(value = "id") int id) {
        this.bookingRoomService.delete(id);
    }

    @DeleteMapping("/booking_rooms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiBooking(@RequestBody List<Map<String, String>> listBookingDelete) {
        List<Integer> ids = new ArrayList<>();
        listBookingDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.bookingRoomService.delete(ids);
    }
}
