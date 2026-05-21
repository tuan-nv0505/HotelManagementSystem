package com.hotel.exceptions;

public class ServiceInUseException extends RuntimeException {
    public ServiceInUseException(String message) {
        super(message);
    }
}
