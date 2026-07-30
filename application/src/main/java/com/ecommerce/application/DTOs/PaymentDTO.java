package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
        Long id,
        String paymentMethod,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {
   public PaymentDTO(Payment entity){
      this(
              entity.getId(),
              entity.getPaymentMethod(),
              entity.getAmount(),
              entity.getStatus(),
              entity.getCreatedAt()
      );
   }
}
