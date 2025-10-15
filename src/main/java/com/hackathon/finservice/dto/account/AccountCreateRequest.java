package com.hackathon.finservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountCreateRequest(
        @NotBlank(message = "Main account number is required")
        String accountNumber,

        @NotBlank(message = "Account type is required")
        String accountType
    ) {
}
