package com.hotel.controleradvice;

import com.hotel.dto.error.ErrorServiceDTO;
import com.hotel.exceptions.ServiceInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ServiceInUseAPIExceptionHandler {
    @ExceptionHandler(ServiceInUseException.class)
    public ResponseEntity<ErrorServiceDTO> ServiceInUseExceptionHandler(ServiceInUseException ex) {
        ErrorServiceDTO errorServiceDTO = new ErrorServiceDTO();

        List<String> details = new ArrayList<>();
        errorServiceDTO.setError(ex.getMessage());
        errorServiceDTO.setDetails(details);

        return new ResponseEntity<>(errorServiceDTO, HttpStatus.BAD_REQUEST);
    }
}
