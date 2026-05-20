package com.hotel.controllers.admin;

import com.hotel.dto.RoomDTO;
import com.hotel.enums.AvailabilityStatus;
import com.hotel.enums.StatusRoom;
import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
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
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private Environment env;

    @GetMapping("/rooms")
    public String RoomView(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("roomDTO", new RoomDTO());
        model.addAttribute("listRoom", this.roomService.list(params));
        model.addAttribute("listStatus", Arrays.asList(StatusRoom.values()));
        model.addAttribute("listAvailabilityStatus", Arrays.asList(AvailabilityStatus.values()));
        model.addAttribute("listRoomType", this.roomTypeService.list(null));

        model.addAttribute("kw", params.get("kw"));

        int pageSize = this.env.getProperty("rooms.page_size", Integer.class, 5);
        long totalRooms = this.roomService.count(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "room";
    }

    @PostMapping("/rooms")
    public String processRoom(@ModelAttribute(name = "roomDTO") RoomDTO roomDTO) {
        this.roomService.addOrUpdate(roomDTO);
        return "redirect:/admin/rooms";
    }
}
