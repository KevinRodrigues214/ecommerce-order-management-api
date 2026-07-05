package com.kevin.managementsystem.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderPaidEvent(
        Long orderId,
        String customerEmail,
        BigDecimal amount
) implements Serializable {}