package com.hotel.services.impl;

import com.hotel.dto.requestbooking.BookingCancelDTO;
import com.hotel.entity.Booking;
import com.hotel.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.hotel.dto.requestbooking.RequestBookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@PropertySource("classpath:secret/mail.properties")
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine emailTemplateEngine;
    @Value("${mail.username}")
    private String mailUserName;

    @Override
    @Async
    public void sendBookingConfirmation(RequestBookingDTO booking) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariable("booking", booking);
            String htmlContent = emailTemplateEngine.process("booking_confirmation", context);

            helper.setTo(booking.getCustomer().getEmail());
            helper.setSubject("Thông Báo: Xác Nhận Đặt Phòng Thành Công tại Spring Hotel");
            helper.setText(htmlContent, true);
            helper.setFrom(this.mailUserName);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Async
    public void sendBookingCancellationDueToTimeout(BookingCancelDTO booking) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariable("booking", booking);
            String htmlContent = emailTemplateEngine.process("booking_cancellation", context);

            helper.setTo(booking.getCustomerEmail());
            helper.setSubject("Thông Báo: Đơn Đặt Phòng Của Bạn Đã Bị Huỷ Do Quá Hạn Thanh Toán");
            helper.setText(htmlContent, true);
            helper.setFrom(this.mailUserName);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
