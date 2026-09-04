package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
    Long id,

    @NotBlank(message = "Payment method is required")
    String paymentMethod,

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than zero")
    BigDecimal amount,

    String status,

    LocalDateTime createdAt
) {
  public static PaymentDTO fromDomain(Payment payment) {
    if (payment == null) {
      return null;
    }
    return new PaymentDTO(
        payment.getId(),
        payment.getPaymentMethod(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getCreatedAt()
    );
  }

  public Payment toDomain() {
    return Payment.builder()
        .id(id)
        .paymentMethod(paymentMethod)
        .amount(amount)
        .status(status)
        .createdAt(createdAt)
        .build();
  }
}
