package com.hotel.controllers.apis;

import com.hotel.dto.wrapper.WrapperDTO;
import com.hotel.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
public class APIServiceController {
    @Autowired
    private Environment env;

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

    @GetMapping("/services")
    public ResponseEntity<WrapperDTO> getServices(@RequestParam Map<String, String> params) {
        int pageSize = this.env.getProperty("services.page_size", Integer.class, 5);
        long totalServices = this.serviceService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);

        WrapperDTO dto = new WrapperDTO();
        dto.setTotalPages(String.valueOf(totalPages));
        dto.setData(this.serviceService.list(params));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
