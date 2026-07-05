package com.kevin.managementsystem.repository;

import com.kevin.managementsystem.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}