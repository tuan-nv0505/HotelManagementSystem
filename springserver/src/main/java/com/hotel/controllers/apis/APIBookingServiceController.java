package com.hotel.controllers.apis;

import com.hotel.services.BookingServiceService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIBookingServiceController {
    @Autowired
    private BookingServiceService bookingServiceService;

    @DeleteMapping("/booking-services/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingService(@PathVariable(value = "id") int id) {
        this.bookingServiceService.delete(id);
    }

    @DeleteMapping("/booking-services")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiBookingService(@RequestBody List<Map<String, String>> listBookingServiceDelete) {
        List<Integer> ids = new ArrayList<>();
        listBookingServiceDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.bookingServiceService.delete(ids);
    }
}
