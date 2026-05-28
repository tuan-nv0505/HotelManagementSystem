package com.hotel.controleradvice;

import com.hotel.exceptions.DuplicateEmailException;
import com.hotel.exceptions.DuplicateUsernameException;
import com.hotel.exceptions.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = com.hotel.controllers.apis.APILoginController.class)
public class UserAPIExceptionHandler {
    @ExceptionHandler({DuplicateUsernameException.class, InvalidPasswordException.class, DuplicateEmailException.class})
    public ResponseEntity<Map<String, Object>> handleUserValidationExceptions(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "FAIL");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleSystemExceptions(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
