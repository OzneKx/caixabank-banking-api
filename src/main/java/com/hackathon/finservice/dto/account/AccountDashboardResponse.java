package com.hackathon.finservice.dto.account;

import java.math.BigDecimal;

public record AccountDashboardResponse(
        String accountNumber,
        BigDecimal balance,
        String accountType
    ) {
}
