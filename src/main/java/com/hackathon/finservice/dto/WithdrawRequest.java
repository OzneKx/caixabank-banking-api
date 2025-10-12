package com.hackathon.finservice.dto;

import java.math.BigDecimal;

public record WithdrawRequest(BigDecimal amount) {
}
