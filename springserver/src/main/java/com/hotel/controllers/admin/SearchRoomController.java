package com.hotel.controllers.admin;

import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class SearchRoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/")
    public String HomeView() {
        return "index";
    }

    @GetMapping("/available-rooms")
    public String roomAvailableView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("listRoomType", roomTypeService.list(null));
        model.addAttribute("listRoom", this.roomService.findAvailableRooms(params));
        return "available_room";
    }
}
