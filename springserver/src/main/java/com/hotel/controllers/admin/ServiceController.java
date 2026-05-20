package com.hotel.controllers.admin;

import com.hotel.dto.ServiceDTO;
import com.hotel.entity.Service;
import com.hotel.services.ServiceService;
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
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private Environment env;

    @GetMapping("/services")
    public String serviceView(Model model, @RequestParam Map<String, String> params) {

        model.addAttribute("serviceDTO", new ServiceDTO());
        model.addAttribute("listService", this.serviceService.list(params));

        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("fromPrice", params.get("fromPrice"));
        model.addAttribute("toPrice", params.get("toPrice"));

        int pageSize = this.env.getProperty("services.page_size", Integer.class, 5);
        long totalServices = this.serviceService.count(params);
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);
        List<Integer> listPage = IntStream.range(0, totalPages).boxed().toList();
        model.addAttribute("listPage", listPage);
        model.addAttribute("currentPage", params.getOrDefault("key", "0"));

        return "service";
    }

    @PostMapping("/services")
    public String processService(@ModelAttribute(name = "serviceDTO") ServiceDTO serviceDTO) {
        this.serviceService.addOrUpdate(serviceDTO);
        return "redirect:/admin/services";
    }
}
