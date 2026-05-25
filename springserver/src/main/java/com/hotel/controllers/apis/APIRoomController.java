package com.hotel.controllers.apis;

import com.hotel.dto.wrapper.WrapperDTO;
import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
public class APIRoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private Environment env;

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

    @GetMapping("/rooms/available")
    public ResponseEntity<WrapperDTO> getListRoom(@RequestParam Map<String, String> params) {
        int pageSize = this.env.getProperty("rooms.page_size", Integer.class, 5);
        long totalRooms = this.roomService.countAvailableRoom(params);
        System.out.println(totalRooms);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);

        WrapperDTO dto = new WrapperDTO();
        dto.setTotalPages(String.valueOf(totalPages));
        dto.setData(this.roomService.findAvailableRooms(params));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
