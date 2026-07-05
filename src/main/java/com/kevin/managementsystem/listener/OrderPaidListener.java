package com.kevin.managementsystem.listener;

import com.kevin.managementsystem.config.RabbitMQConfig;
import com.kevin.managementsystem.domain.Order;
import com.kevin.managementsystem.dto.OrderPaidEvent;
import com.kevin.managementsystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPaidListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_PAID_QUEUE)
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("Processando pedido pago: orderId={}, cliente={}, valor={}",
                event.orderId(), event.customerEmail(), event.amount());

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + event.orderId()));

        // Simula processamento pós-pagamento (poderia ser: gerar nota fiscal, notificar estoque, mandar email)
        order.setStatus(Order.OrderStatus.PROCESSING);
        orderRepository.save(order);

        log.info("Pedido {} atualizado para PROCESSING", event.orderId());

        // Aqui em um sistema real você poderia:
        // - Enviar email de confirmação
        // - Notificar sistema de logística
        // - Gerar nota fiscal
    }
}