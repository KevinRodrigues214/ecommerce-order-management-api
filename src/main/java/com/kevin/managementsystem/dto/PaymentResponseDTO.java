package com.kevin.managementsystem.dto;

import com.kevin.managementsystem.domain.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,
        Long orderId,
        BigDecimal amount,
        String status,
        String method,
        LocalDateTime paidAt
) {
    public static PaymentResponseDTO fromEntity(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus().name(),
                payment.getMethod(),
                payment.getPaidAt()
        );
    }
}