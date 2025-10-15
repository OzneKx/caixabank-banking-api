package com.hackathon.finservice.dto;

import java.math.BigDecimal;

public record TransferRequest(BigDecimal amount, String targetAccountNumber) {
}
