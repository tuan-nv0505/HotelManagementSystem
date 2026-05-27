package com.hotel.controllers.admin;

import com.hotel.services.RoomService;
import com.hotel.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PropertySource("classpath:/configs.properties")
public class SearchRoomController {
    @Autowired
    private Environment env;

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/")
    public String HomeView(Model model) {
        return "index";
    }

    @GetMapping("/available-rooms")
    public String roomAvailableView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("listRoomType", roomTypeService.list(null));
        model.addAttribute("listRoom", this.roomService.findAvailableRooms(params));

        model.addAttribute("roomTypeId", params.get("roomTypeId"));
        model.addAttribute("expectedCheckIn", params.get("expectedCheckIn"));
        model.addAttribute("expectedCheckOut", params.get("expectedCheckOut"));

        int pageSize = this.env.getProperty("rooms.page_size", Integer.class, 5);
        long totalRooms = this.roomService.countAvailableRoom(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
        List<Integer> listPage = IntStream.range(1, totalPages + 1).boxed().toList();
        System.out.println(totalRooms);
        System.out.println(pageSize);
        System.out.println(totalPages);
        System.out.println(listPage);
        model.addAttribute("listPage", listPage);

        return "available_room";
    }
}
