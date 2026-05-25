package com.hotel.services.impl;

import com.hotel.entity.Booking;
import com.hotel.entity.Payment;
import com.hotel.enums.StatusBooking;
import com.hotel.repositories.BookingRepository;
import com.hotel.repositories.PaymentRepository;
import com.hotel.services.VnPayService;
import com.hotel.utils.VnpaySecurityUtil;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@PropertySource("classpath:secret/vnpay.properties")
public class VnPayServiceImpl implements VnPayService {
    @Value("${vnp_TmnCode}")
    private String vnpTmnCode;
    @Value("${vnp_HashSecret}")
    private String vnpHashSecret;
    @Value("${vnp_PayUrl}")
    private String vnpPayUrl;
    @Value("${vnp_ReturnUrl}")
    private String vnpReturnUrl;
    @Value("${vnp_ApiUrl}")
    private String vnpApiUrl;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public String createPaymentUrl(int bookingId, HttpServletRequest request) throws Exception {
        Booking booking = bookingRepository.get(bookingId);
        if (booking == null) throw new RuntimeException("Đơn đặt phòng không tồn tại!");

        long amount = booking.getTotalAmount().longValue();

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(bookingId));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + bookingId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_IpAddr", getClientIp(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = vnp_Params.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VnpaySecurityUtil.hashRawString(hashData.toString(), vnpHashSecret);
        return vnpPayUrl + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
    }


    @Override
    public Map<String, String> processIpn(Map<String, String> params) {
        Map<String, String> response = new HashMap<>();
        try {
            String vnp_SecureHash = params.get("vnp_SecureHash");
            Map<String, String> vnp_Params = new TreeMap<>(params);
            vnp_Params.remove("vnp_SecureHash");
            vnp_Params.remove("vnp_SecureHashType");

            StringBuilder hashData = new StringBuilder();
            Iterator<Map.Entry<String, String>> itr = vnp_Params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                if ((entry.getValue() != null) && (entry.getValue().length() > 0)) {
                    hashData.append(entry.getKey()).append("=").append(entry.getValue());
                    if (itr.hasNext()) {
                        hashData.append("&");
                    }
                }
            }

            String signValue = VnpaySecurityUtil.hashRawString(hashData.toString(), vnpHashSecret);

            if (signValue.equals(vnp_SecureHash)) {
                int bookingId = Integer.parseInt(vnp_Params.get("vnp_TxnRef"));
                Booking booking = bookingRepository.get(bookingId);

                if (booking != null) {
                    if ("PENDING".equals(booking.getStatus().toString())) {
                        if ("00".equals(vnp_Params.get("vnp_ResponseCode"))) {
                            booking.setStatus(StatusBooking.CONFIRMED.name());
                            Payment payment = new Payment();
                            payment.setBooking(booking);
                            payment.setAmount(booking.getTotalAmount());
                            payment.setTransactionCode(vnp_Params.get("vnp_TransactionNo"));
                            payment.setNote("Thành công qua VNPAY");
                            paymentRepository.save(payment);
                        } else {
                            booking.setStatus(StatusBooking.CANCELLED.name());
                        }
                        bookingRepository.addOrUpdate(booking);
                        response.put("RspCode", "00");
                        response.put("Message", "Confirm Success");
                    } else {
                        response.put("RspCode", "02");
                        response.put("Message", "Order already confirmed");
                    }
                } else {
                    response.put("RspCode", "01");
                    response.put("Message", "Order not found");
                }
            } else {
                response.put("RspCode", "97");
                response.put("Message", "Invalid Checksum");
            }
        } catch (Exception e) {
            response.put("RspCode", "99");
            response.put("Message", "Unknown Error");
        }
        return response;
    }

    @Override
    public String queryTransaction(int bookingId, String transDate, HttpServletRequest request) throws Exception {
        String vnp_RequestId = VnpaySecurityUtil.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TxnRef = String.valueOf(bookingId);
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_IpAddr = getClientIp(request);

        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnpTmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", transDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnpTmnCode, vnp_TxnRef, transDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = VnpaySecurityUtil.hashRawString(hash_Data, vnpHashSecret);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        return sendPostRequest(vnpApiUrl, vnp_Params.toString());
    }

    @Override
    public String refundTransaction(int bookingId, String transDate, String user, HttpServletRequest request) throws Exception {
        Booking booking = bookingRepository.get(bookingId);
        if (booking == null) throw new RuntimeException("Đơn đặt phòng không tồn tại!");

        String vnp_RequestId = VnpaySecurityUtil.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TransactionType = "02";
        String vnp_TxnRef = String.valueOf(bookingId);
        long amount = booking.getTotalAmount().longValue() * 100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_IpAddr = getClientIp(request);

        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnpTmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", transDate);
        vnp_Params.addProperty("vnp_CreateBy", user);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnpTmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, "", transDate, user, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = VnpaySecurityUtil.hashRawString(hash_Data, vnpHashSecret);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        return sendPostRequest(vnpApiUrl, vnp_Params.toString());
    }

    private String sendPostRequest(String urlPath, String jsonData) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonData);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        return response.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) return "127.0.0.1";
        return ip;
    }

}
