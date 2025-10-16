package com.hackathon.finservice.dto.transaction;

import java.math.BigDecimal;

public record TransferRequest(BigDecimal amount, String targetAccountNumber) {
}
