package com.hotel.controleradvice;

import com.hotel.dto.error.ErrorDTO;
import com.hotel.exceptions.NotFoundUser;
import com.hotel.exceptions.ServiceInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(ServiceInUseException.class)
    public ResponseEntity<ErrorDTO> serviceInUseExceptionHandler(ServiceInUseException ex) {
        ErrorDTO errorServiceDTO = new ErrorDTO();

        List<String> details = new ArrayList<>();
        errorServiceDTO.setError(ex.getMessage());
        errorServiceDTO.setDetails(details);

        return new ResponseEntity<>(errorServiceDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundUser.class)
    public ResponseEntity<ErrorDTO> notFoundUserExceptionHandler(NotFoundUser ex) {
        ErrorDTO error = new ErrorDTO();

        List<String> details = new ArrayList<>();
        error.setError(ex.getMessage());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
