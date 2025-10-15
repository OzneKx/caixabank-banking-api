package com.hackathon.finservice.dto;

public record UserDashboardResponse(
        String name,
        String email,
        String accountNumber,
        String accountType,
        String hashedPassword
    ) {
}
