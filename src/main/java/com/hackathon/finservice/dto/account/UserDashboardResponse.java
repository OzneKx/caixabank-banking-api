package com.hackathon.finservice.dto.account;

public record UserDashboardResponse(
        String name,
        String email,
        String accountNumber,
        String accountType,
        String hashedPassword
    ) {
}
