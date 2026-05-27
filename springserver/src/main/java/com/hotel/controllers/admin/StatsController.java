package com.hotel.controllers.admin;

import com.hotel.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueStats(@RequestParam("type") String type, @RequestParam("year") int year) {
        return ResponseEntity.ok(statsService.getRevenue(type, year));
    }

    @GetMapping("/occupancy")
    public ResponseEntity<?> getOccupancyRate(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        double rate = statsService.getOccupancyRate(targetDate);

        Map<String, Object> response = new HashMap<>();
        response.put("occupancyRate", rate);
        return ResponseEntity.ok(response);
    }
}