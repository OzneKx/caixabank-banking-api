package com.hackathon.finservice.dto;

import com.hackathon.finservice.data.entity.TransactionStatus;
import com.hackathon.finservice.data.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        Instant transactionDate,
        String sourceAccountNumber,
        String targetAccountNumber
) {
}
