package com.hotel.controllers.apis;

import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIRoomController {
    @Autowired
    private RoomService roomService;

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingService(@PathVariable(value = "id") int id) {
        this.roomService.delete(id);
    }

    @DeleteMapping("/rooms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiBookingService(@RequestBody List<Map<String, String>> listRoomDelete) {
        List<Integer> ids = new ArrayList<>();
        listRoomDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.roomService.delete(ids);
    }
}
