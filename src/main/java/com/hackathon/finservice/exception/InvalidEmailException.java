package com.hackathon.finservice.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Invalid email: " + email);
    }
}
