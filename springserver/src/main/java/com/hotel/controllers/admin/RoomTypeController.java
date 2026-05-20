package com.hotel.controllers.admin;

import com.hotel.dto.RoomTypeDTO;
import com.hotel.services.RoomTypeService;
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
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private Environment env;

    @GetMapping("/room-types")
    public String RoomTypeView(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("RoomType", new RoomTypeDTO());
        model.addAttribute("listRoomType", this.roomTypeService.listRoomType(params));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("fromPrice", params.get("fromPrice"));
        model.addAttribute("toPrice", params.get("toPrice"));

        int pageSize = this.env.getProperty("room_types.page_size", Integer.class, 5);
        long totalRoomTypes = this.roomTypeService.countRoomType(params);
        int totalPages = (int) Math.ceil((double) totalRoomTypes / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "room_type";
    }

    @PostMapping("/room-types")
    public String processRoomType(@ModelAttribute(name = "roomType") RoomTypeDTO roomTypeDTO) {
        this.roomTypeService.addOrUpdateRoomType(roomTypeDTO);
        return "redirect:/admin/room-types";
    }
}
