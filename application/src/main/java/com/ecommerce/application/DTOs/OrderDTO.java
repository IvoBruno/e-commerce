package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.Order;

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
   public OrderDTO(Order entity){
      this(
              entity.getId(),
              entity.getUser().getId(),
              entity.getTotalAmount(),
              entity.getCreatedAt(),
              entity.getStatus(),
              entity.getPayment().getId()
      );
   }
}
