package com.hotel.utils;

import com.hotel.entity.Booking;
import com.hotel.repositories.BookingRepository;
import com.hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingCleanupScheduler {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 60000)
    public void autoCancelBookings() {
        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(15);
        for (Booking b : expiredBookings) {
            bookingService.processExpiredBooking(b.getId());

            String message = "Hệ thống vừa tự động hủy booking #" + b.getId() + " do quá hạn thanh toán.";
            messagingTemplate.convertAndSend("/topic/bookings", message);
            System.out.println("Đã hủy tự động booking ID: " + b.getId());
        }
    }
}
