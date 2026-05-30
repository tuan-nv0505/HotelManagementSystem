package com.hotel.utils;

import com.hotel.dto.requestbooking.BookingCancelDTO;
import com.hotel.entity.Booking;
import com.hotel.repositories.BookingRepository;
import com.hotel.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class BookingCleanupScheduler {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MailService mailService;

    //    @Scheduled(fixedRate = 10000)
    @Scheduled(fixedRate = 60000)
    public void autoCancelBookings() {
        try {
            List<Booking> expiredBookings = bookingRepository.findExpiredBookings(15);

            if (expiredBookings == null || expiredBookings.isEmpty()) {
                return;
            }

            for (Booking b : expiredBookings) {
                try {
                    BookingCancelDTO dto = new BookingCancelDTO();

                    if (b.getCustomer() != null) {
                        dto.setCustomerName(b.getCustomer().getName());
                        dto.setCustomerEmail(b.getCustomer().getEmail());
                    } else {
                        dto.setCustomerName("Quý khách");
                        dto.setCustomerEmail("");
                    }

                    dto.setExpectedCheckIn(b.getExpectedCheckIn().toString());
                    dto.setExpectedCheckOut(b.getExpectedCheckOut().toString());
                    dto.setTotalAmount(b.getTotalAmount().toString());

                    bookingRepository.processExpiredBooking(b.getId());

                    if (!dto.getCustomerEmail().isEmpty()) {
                        mailService.sendBookingCancellationDueToTimeout(dto);
                    }

                    String message = "Hệ thống vừa tự động hủy booking #" + b.getId() + " do quá hạn thanh toán.";
                    messagingTemplate.convertAndSend("/topic/bookings", message);

                    System.out.println("Đã hủy tự động booking ID: " + b.getId());
                } catch (Exception e) {
                    System.err.println("Lỗi khi tự động hủy booking #" + b.getId() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi toàn cục trong Scheduler lấy danh sách booking: " + e.getMessage());
        }
    }
}
