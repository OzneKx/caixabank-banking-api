package com.hackathon.finservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User not found for the given identifier: " + email);
    }
}
