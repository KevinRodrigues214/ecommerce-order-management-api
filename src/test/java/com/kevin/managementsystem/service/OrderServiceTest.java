package com.kevin.managementsystem.service;

import com.kevin.managementsystem.domain.Product;
import com.kevin.managementsystem.domain.User;
import com.kevin.managementsystem.dto.OrderItemRequestDTO;
import com.kevin.managementsystem.dto.OrderRequestDTO;
import com.kevin.managementsystem.dto.OrderResponseDTO;
import com.kevin.managementsystem.exception.BusinessException;
import com.kevin.managementsystem.repository.OrderRepository;
import com.kevin.managementsystem.repository.ProductRepository;
import com.kevin.managementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = User.builder()
                .id(1L)
                .name("Maria Souza")
                .email("maria@email.com")
                .role(User.Role.CUSTOMER)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Notebook Dell")
                .price(BigDecimal.valueOf(3500.00))
                .stockQuantity(10)
                .build();

        // Simula que existe alguém "logado" durante o teste
        var auth = new UsernamePasswordAuthenticationToken(customer.getEmail(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
    }

    @Test
    void deveCriarPedidoComSucessoQuandoTemEstoque() {
        // Arrange
        var itemDto = new OrderItemRequestDTO(1L, 2);
        var orderDto = new OrderRequestDTO(List.of(itemDto));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderResponseDTO result = orderService.create(orderDto);

        // Assert
        assertThat(result.totalAmount()).isEqualByComparingTo(BigDecimal.valueOf(7000.00));
        assertThat(result.status()).isEqualTo("PENDING");
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        // Arrange
        var itemDto = new OrderItemRequestDTO(1L, 999); // mais do que tem em estoque
        var orderDto = new OrderRequestDTO(List.of(itemDto));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act + Assert
        assertThatThrownBy(() -> orderService.create(orderDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Estoque insuficiente");
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        var itemDto = new OrderItemRequestDTO(999L, 1);
        var orderDto = new OrderRequestDTO(List.of(itemDto));

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Produto não encontrado");
    }
}