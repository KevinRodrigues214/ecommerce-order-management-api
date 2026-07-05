package com.kevin.managementsystem.service;

import com.kevin.managementsystem.config.RabbitMQConfig;
import com.kevin.managementsystem.domain.Order;
import com.kevin.managementsystem.domain.Payment;
import com.kevin.managementsystem.dto.OrderPaidEvent;
import com.kevin.managementsystem.dto.PaymentRequestDTO;
import com.kevin.managementsystem.dto.PaymentResponseDTO;
import com.kevin.managementsystem.exception.BusinessException;
import com.kevin.managementsystem.repository.OrderRepository;
import com.kevin.managementsystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new BusinessException("Pedido não encontrado: " + dto.orderId()));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BusinessException("Este pedido não está aguardando pagamento. Status atual: " + order.getStatus());
        }

        // Simulação: pagamento sempre aprovado (poderia ser aleatório pra simular falha, ex: 90% aprova)
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(Payment.PaymentStatus.APPROVED)
                .method(dto.method())
                .paidAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Atualiza status do pedido pra PAID (isso acontece na hora, é síncrono)
        order.setStatus(Order.OrderStatus.PAID);
        orderRepository.save(order);

        // Publica evento na fila -- isso é assíncrono, o listener processa depois
        OrderPaidEvent event = new OrderPaidEvent(
                order.getId(),
                order.getUser().getEmail(),
                order.getTotalAmount()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_PAID_QUEUE, event);

        return PaymentResponseDTO.fromEntity(savedPayment);
    }
}