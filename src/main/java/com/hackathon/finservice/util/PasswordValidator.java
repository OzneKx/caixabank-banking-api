package com.hackathon.finservice.util;

import com.hackathon.finservice.exception.InvalidPasswordException;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidPasswordException("Password cannot be empty");
        }

        if (password.length() < MIN_LENGTH) {
            throw new InvalidPasswordException("Password must be at least 8 characters long");
        }

        if (password.length() > MAX_LENGTH) {
            throw new InvalidPasswordException("Password must be less than 128 characters long");
        }

        if (password.matches(".*\\s.*")) {
            throw new InvalidPasswordException("Password cannot contain whitespace");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new InvalidPasswordException("Password must contain at least one digit and one special character");
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>\\-_=+].*")) {
            throw new InvalidPasswordException("Password must contain at least one special character");
        }
    }
}
