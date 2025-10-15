package com.hackathon.finservice.dto;

import java.math.BigDecimal;

public record AccountDashboardResponse(
        String accountNumber,
        BigDecimal balance,
        String accountType
    ) {
}
