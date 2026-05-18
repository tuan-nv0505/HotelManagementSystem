package com.hotel.controllers.admin;

import com.hotel.entity.Service;
import com.hotel.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/services")
    public String ServiceView(Model model) {
        model.addAttribute("service", new Service());
        model.addAttribute("listService", this.serviceService.listService());
        return "service";
    }

    @PostMapping("/services")
    public String processService(@ModelAttribute(name = "service") Service service) {
        System.out.println(service.getId());
        System.out.println(service.getName());
        System.out.println(service.getActive());
        System.out.println(service.getPrice());
        return "service";
    }
}
