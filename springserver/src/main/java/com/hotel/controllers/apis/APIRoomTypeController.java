package com.hotel.controllers.apis;


import com.hotel.dto.wrapper.WrapperDTO;
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
public class APIRoomTypeController {
    @Autowired
    private Environment env;

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/room-types")
    public ResponseEntity<WrapperDTO> getRoomTypes(@RequestParam Map<String, String> params) {
        int pageSize = this.env.getProperty("room_types.page_size", Integer.class, 5);
        long totalRoomTypes = this.roomTypeService.count(params);
        int totalPages = (int) Math.ceil((double) totalRoomTypes / pageSize);

        WrapperDTO dto = new WrapperDTO();
        dto.setTotalPages(String.valueOf(totalPages));
        dto.setData(this.roomTypeService.list(params));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/room-types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomType(@PathVariable(value = "id") int id) {
        this.roomTypeService.delete(id);
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

        this.roomTypeService.delete(ids);
    }
}
