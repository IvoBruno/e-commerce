package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Order;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
    Long id,

    @NotNull(message = "User ID is required")
    Long user_id,

    @PositiveOrZero(message = "Total amount cannot be negative")
    BigDecimal totalAmount,

    LocalDateTime createdAt,

    String status,

    Long payment_id
) {
  public static OrderDTO fromDomain(Order order) {
    if (order == null) {
      return null;
    }
    return new OrderDTO(
        order.getId(),
        order.getUserId(),
        order.getTotalAmount(),
        order.getCreatedAt(),
        order.getStatus(),
        order.getPaymentId()
    );
  }

  public Order toDomain() {
    return Order.builder()
        .id(id)
        .userId(user_id)
        .totalAmount(totalAmount)
        .createdAt(createdAt)
        .status(status)
        .paymentId(payment_id)
        .build();
  }
}
