package com.hotel.controleradvice;

import com.hotel.exceptions.NotFoundBookingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = com.hotel.controllers.admin.BookingServiceController.class)
public class BookingServiceItemViewExceptionHandler {
    @ExceptionHandler(NotFoundBookingException.class)
    public String NotFoundBookingExceptionHandler(NotFoundBookingException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                ex.getMessage()
        );

        return "redirect:/admin/booking-services";
    }
}
