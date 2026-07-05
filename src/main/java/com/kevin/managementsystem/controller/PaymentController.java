package com.kevin.managementsystem.controller;

import com.kevin.managementsystem.dto.PaymentRequestDTO;
import com.kevin.managementsystem.dto.PaymentResponseDTO;
import com.kevin.managementsystem.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.processPayment(dto));
    }
}