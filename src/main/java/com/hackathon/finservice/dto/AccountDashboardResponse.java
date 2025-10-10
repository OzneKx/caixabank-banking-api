package com.hackathon.finservice.dto;

public record AccountDashboardResponse(
        String accountNumber,
        double balance,
        String accountType
    ) {
}
