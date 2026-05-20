package com.hotel.controllers.admin;

import com.hotel.dto.UserDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.enums.RoleUser;
import com.hotel.services.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/users")
    public String userView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roleUsers", RoleUser.values());
        model.addAttribute("listUsers", userService.listUser(params));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("phone", params.get("phone"));

        int pageSize = this.env.getProperty("users.page_size", Integer.class, 5);
        long totalServices = this.userService.countUser(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "user";
    }

    @PostMapping("/users")
    public String processUser(@ModelAttribute(name = "user") UserDTO userDTO) {
        userService.addOrUpdateUser(userDTO);
        return "user";
    }
}
