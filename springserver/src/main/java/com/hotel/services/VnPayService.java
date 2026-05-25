package com.hotel.services;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VnPayService {
    String createPaymentUrl(int bookingId, HttpServletRequest request) throws Exception;

    Map<String, String> processIpn(Map<String, String> params);

    String queryTransaction(int bookingId, String transDate, HttpServletRequest request) throws Exception;

    String refundTransaction(int bookingId, String transDate, String user, HttpServletRequest request) throws Exception;
}
