package com.ecommerce.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.application.ports.output.OrderRepositoryPort;
import com.ecommerce.application.ports.output.PaymentRepositoryPort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.application.service.OrderApplicationService;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.valueobjects.OrderStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceTest {

  @Mock
  private OrderRepositoryPort orderRepositoryPort;

  @Mock
  private UserRepositoryPort userRepositoryPort;

  @Mock
  private PaymentRepositoryPort paymentRepositoryPort;

  @InjectMocks
  private OrderApplicationService orderApplicationService;

  @Test
  @DisplayName("Should create order successfully when user exists")
  void shouldCreateOrderSuccessfully() {
    Long userId = 10L;
    Order inputOrder = Order.builder()
        .userId(userId)
        .totalAmount(new BigDecimal("100.00"))
        .build();

    when(userRepositoryPort.existsById(userId)).thenReturn(true);
    when(orderRepositoryPort.save(any(Order.class))).thenAnswer(invocation -> {
      Order saved = invocation.getArgument(0);
      saved.setId(1L);
      return saved;
    });

    Order result = orderApplicationService.create(inputOrder);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals(OrderStatus.WAITING_PAYMENT.name(), result.getStatus());
    assertNotNull(result.getCreatedAt());
    verify(orderRepositoryPort).save(any(Order.class));
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when user does not exist")
  void shouldThrowWhenUserNotFound() {
    Long userId = 999L;
    Order inputOrder = Order.builder()
        .userId(userId)
        .build();

    when(userRepositoryPort.existsById(userId)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> orderApplicationService.create(inputOrder));
  }
}

