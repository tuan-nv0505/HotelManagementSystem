package com.hotel.controleradvice;

import com.hotel.exceptions.DuplicateUsernameException;
import com.hotel.exceptions.InvalidPasswordException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = com.hotel.controllers.admin.UserController.class)
public class UserViewExceptionHandler {
    @ExceptionHandler(DuplicateUsernameException.class)
    public String handleDuplicateUsername(DuplicateUsernameException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/users";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPassword(InvalidPasswordException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/admin/users";
    }
}
