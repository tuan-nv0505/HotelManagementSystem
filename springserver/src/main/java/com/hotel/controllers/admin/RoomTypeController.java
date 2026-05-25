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

        model.addAttribute("roomTypeDTO", new RoomTypeDTO());
        model.addAttribute("listRoomType", this.roomTypeService.list(params));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("fromPrice", params.get("fromPrice"));
        model.addAttribute("toPrice", params.get("toPrice"));

        int pageSize = this.env.getProperty("room_types.page_size", Integer.class, 5);
        long totalRoomTypes = this.roomTypeService.count(params);
        int totalPages = (int) Math.ceil((double) totalRoomTypes / pageSize);
        List<Integer> listPage = IntStream.range(1, totalPages + 1).boxed().toList();
        model.addAttribute("listPage", listPage);

        return "room_type";
    }

    @PostMapping("/room-types")
    public String process(@ModelAttribute(name = "roomTypeDTO") RoomTypeDTO roomTypeDTO) {
        this.roomTypeService.addOrUpdate(roomTypeDTO);
        return "redirect:/admin/room-types";
    }
}
