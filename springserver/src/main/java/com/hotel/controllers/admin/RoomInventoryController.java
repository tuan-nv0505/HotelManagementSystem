package com.hotel.controllers.admin;

import com.hotel.services.RoomInventoryService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class RoomInventoryController {
    @Autowired
    private RoomInventoryService roomInventoryService;
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/")
    public String HomeView() {
        return "index";
    }

    @GetMapping("/roominventory")
    public String RoomInventoryView(
            Model model,
            @RequestParam(name = "roomTypeId", required = false) Integer roomTypeId,
            @RequestParam(name = "inventoryDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inventoryDate
    ) {
        model.addAttribute("listRoomType", roomTypeService.listRoomType(null));
        if (roomTypeId != null || inventoryDate != null) {
            model.addAttribute("listRoomInventory",
                    roomInventoryService.getListRoomInventory(roomTypeId, inventoryDate));
        }

        return "room_inventory";
    }
}
