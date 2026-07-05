package com.kevin.managementsystem.service;

import com.kevin.managementsystem.domain.*;
import com.kevin.managementsystem.dto.OrderRequestDTO;
import com.kevin.managementsystem.dto.OrderResponseDTO;
import com.kevin.managementsystem.exception.BusinessException;
import com.kevin.managementsystem.repository.OrderRepository;
import com.kevin.managementsystem.repository.ProductRepository;
import com.kevin.managementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {
        User currentUser = getCurrentUser();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(currentUser)
                .status(Order.OrderStatus.PENDING)
                .build();

        for (var itemDto : dto.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new BusinessException("Produto não encontrado: " + itemDto.productId()));

            if (product.getStockQuantity() < itemDto.quantity()) {
                throw new BusinessException(
                        "Estoque insuficiente para o produto: " + product.getName() +
                                " (disponível: " + product.getStockQuantity() + ")"
                );
            }

            // dá baixa no estoque
            product.setStockQuantity(product.getStockQuantity() - itemDto.quantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.quantity())
                    .unitPrice(product.getPrice()) // preço travado no momento da compra
                    .build();

            orderItems.add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        return OrderResponseDTO.fromEntity(saved);
    }

    public List<OrderResponseDTO> findMyOrders() {
        User currentUser = getCurrentUser();
        return orderRepository.findByUserId(currentUser.getId())
                .stream()
                .map(OrderResponseDTO::fromEntity)
                .toList();
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponseDTO::fromEntity)
                .toList();
    }

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado: " + id));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new BusinessException("Você não tem permissão para ver este pedido");
        }

        return OrderResponseDTO.fromEntity(order);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}