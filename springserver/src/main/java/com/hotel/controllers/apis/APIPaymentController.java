package com.hotel.controllers.apis;

import com.hotel.services.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class APIPaymentController {

    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestParam("bookingId") int bookingId, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String paymentUrl = vnPayService.createPaymentUrl(bookingId, request);
            response.put("status", "OK");
            response.put("message", "Tạo URL thanh toán thành công");
            response.put("url", paymentUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Lỗi tạo thanh toán: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/vnpay-ipn")
    public Map<String, String> receiveIpn(@RequestParam Map<String, String> allParams) {
        return vnPayService.processIpn(allParams);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Map<String, Object>> paymentReturn(@RequestParam Map<String, String> allParams) {
        Map<String, Object> response = new HashMap<>();
        String responseCode = allParams.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            response.put("status", "SUCCESS");
            response.put("message", "Chúc mừng! Bạn đã thanh toán tiền phòng thành công.");
        } else {
            response.put("status", "FAIL");
            response.put("message", "Thanh toán không thành công hoặc đã bị hủy bỏ.");
        }
        response.put("data", allParams);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> queryTransaction(
            @RequestParam("bookingId") int bookingId,
            @RequestParam("transDate") String transDate,
            HttpServletRequest request) {
        try {
            String result = vnPayService.queryTransaction(bookingId, transDate, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping(value = "/refund", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> refundTransaction(
            @RequestParam("bookingId") int bookingId,
            @RequestParam("transDate") String transDate,
            @RequestParam("user") String user,
            HttpServletRequest request) {
        try {
            String result = vnPayService.refundTransaction(bookingId, transDate, user, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
