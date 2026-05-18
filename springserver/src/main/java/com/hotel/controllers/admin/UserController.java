package com.hotel.controllers.admin;

import com.hotel.entity.Customer;
import com.hotel.entity.User;
import com.hotel.enums.RoleUser;
import com.hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/users")
    public String userView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleUsers", RoleUser.values());
        model.addAttribute("listUsers", userService.getAllUsers());
        return "user";
    }

    @PostMapping("/users")
    public String processUser(@ModelAttribute(name = "user") User user) {
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getActive());
        return "user";
    }
}
