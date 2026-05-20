package com.hotel.controllers.apis;

import com.hotel.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIServiceController {
    @Autowired
    private ServiceService serviceService;

    @DeleteMapping("/services/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable(value = "id") int id) {
        this.serviceService.delete(id);
    }

    @DeleteMapping("/services")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiService(@RequestBody List<Map<String, String>> listServiceDelete) {
        List<Integer> ids = new ArrayList<>();
        listServiceDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.serviceService.delete(ids);
    }
}
