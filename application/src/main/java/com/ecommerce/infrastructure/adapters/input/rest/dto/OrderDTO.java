package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
    Long id,
    Long user_id,
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

