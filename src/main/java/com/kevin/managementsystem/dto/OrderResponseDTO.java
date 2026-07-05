package com.kevin.managementsystem.dto;

import com.kevin.managementsystem.domain.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String customerEmail,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponseDTO> items,
        LocalDateTime createdAt
) {
    public static OrderResponseDTO fromEntity(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getEmail(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getItems().stream().map(OrderItemResponseDTO::fromEntity).toList(),
                order.getCreatedAt()
        );
    }
}