package com.hotel.exceptions;

public class NotFoundUser extends RuntimeException {
    public NotFoundUser(String message) {
        super(message);
    }
}
