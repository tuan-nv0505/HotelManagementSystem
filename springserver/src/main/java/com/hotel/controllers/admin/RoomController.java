package com.hotel.controllers.admin;

import com.hotel.dto.RoomDTO;
import com.hotel.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private Environment env;

    @GetMapping("/rooms")
    public String RoomView(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("Room", new RoomDTO());
        model.addAttribute("listRoom", this.roomService.listRoom(params));

        model.addAttribute("kw", params.get("kw"));

        int pageSize = this.env.getProperty("room.page_size", Integer.class, 5);
        long totalRooms = this.roomService.countRoom(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "room";
    }

    @PostMapping("/rooms")
    public String processRoom(@ModelAttribute(name = "room") RoomDTO roomDTO) {
        this.roomService.addOrUpdateRoom(roomDTO);
        return "redirect:/admin/rooms";
    }
}
