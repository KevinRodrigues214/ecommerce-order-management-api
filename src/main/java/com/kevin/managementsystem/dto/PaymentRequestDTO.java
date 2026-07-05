package com.kevin.managementsystem.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDTO(
        @NotNull Long orderId,
        @NotBlank String method // "CREDIT_CARD", "PIX", etc (simulado)
) {}