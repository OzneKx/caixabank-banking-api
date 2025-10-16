package com.hackathon.finservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String email) {
        super("User not found for the given identifier: " + email);
    }
}
