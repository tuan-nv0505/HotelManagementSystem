package com.hotel.controllers.apis;

import com.hotel.services.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class APIPublicPaymentController {

    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/vnpay-ipn")
    public Map<String, String> receiveIpn(@RequestParam Map<String, String> allParams) {
        return vnPayService.processIpn(allParams);
    }

//    @GetMapping("/vnpay-return")
//    public ResponseEntity<String> paymentReturn(@RequestParam Map<String, String> allParams) {
//        String responseCode = allParams.get("vnp_ResponseCode");
//        boolean isSuccess = "00".equals(responseCode);
//
//        String message = isSuccess ? "Giao dịch thành công!" : "Giao dịch thất bại hoặc bị hủy!";
//        String color = isSuccess ? "#28a745" : "#dc3545";
//
//        String html = "<!DOCTYPE html>" +
//                "<html>" +
//                "<head><title>Kết quả thanh toán</title><meta charset='utf-8'></head>" +
//                "<body style='display:flex; justify-content:center; align-items:center; height:100vh; font-family:sans-serif; text-align:center; background-color: #f8fbff;'>" +
//                "<div>" +
//                "    <h2 style='color: " + color + "'>" + message + "</h2>" +
//                "    <p style='color: #6c757d;'>Cửa sổ này sẽ tự động đóng sau giây lát...</p>" +
//                "</div>" +
//                "<script>" +
//                "    setTimeout(function() {" +
//                "        window.close();" +
//                "    }, 2500);" +
//                "</script>" +
//                "</body>" +
//                "</html>";
//
//        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
//    }
}
