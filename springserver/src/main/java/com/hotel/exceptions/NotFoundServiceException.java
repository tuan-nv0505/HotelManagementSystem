package com.hotel.exceptions;

public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException(String message) {
        super(message);
    }
}
