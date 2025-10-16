package com.hackathon.finservice.dto.transaction;

import java.math.BigDecimal;

public record DepositRequest(BigDecimal amount) {
}
