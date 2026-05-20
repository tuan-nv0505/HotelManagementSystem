package com.hotel.controllers.apis;

import com.hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class APIUserController {
    @Autowired
    private UserService userService;

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable(value = "id") int id) {
        this.userService.deleteUser(id);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiCustomer(@RequestBody List<Map<String, String>> listCustomerDelete) {
        List<Integer> ids = new ArrayList<>();
        listCustomerDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.userService.deleteUser(ids);
    }
}
