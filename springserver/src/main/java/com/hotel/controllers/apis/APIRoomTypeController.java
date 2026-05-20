package com.hotel.controllers.apis;


import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIRoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;

    @DeleteMapping("/room-types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomType(@PathVariable(value = "id") int id) {
        this.roomTypeService.deleteRoomType(id);
    }

    @DeleteMapping("/room-types")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiRoomType(@RequestBody List<Map<String, String>> listRoomTypeDelete) {
        List<Integer> ids = new ArrayList<>();
        listRoomTypeDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.roomTypeService.deleteRoomType(ids);
    }
}
