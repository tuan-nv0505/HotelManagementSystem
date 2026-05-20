package com.hotel.controllers.apis;

import com.hotel.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APICustomerController {
    @Autowired
    private CustomerService customerService;

    @DeleteMapping("/customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable(value = "id") int id) {
        this.customerService.deleteCustomer(id);
    }

    @DeleteMapping("/customers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiCustomer(@RequestBody List<Map<String, String>> listCustomerDelete) {
        List<Integer> ids = new ArrayList<>();
        listCustomerDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.customerService.deleteCustomer(ids);
    }
}
