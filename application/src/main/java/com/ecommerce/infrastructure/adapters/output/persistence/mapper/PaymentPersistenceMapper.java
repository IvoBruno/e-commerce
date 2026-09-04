package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.Payment;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

  public Payment toDomain(PaymentJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return Payment.builder()
        .id(entity.getId())
        .paymentMethod(entity.getPaymentMethod())
        .amount(entity.getAmount())
        .status(entity.getStatus())
        .createdAt(entity.getCreatedAt())
        .build();
  }

  public PaymentJpaEntity toJpaEntity(Payment domain) {
    if (domain == null) {
      return null;
    }
    return PaymentJpaEntity.builder()
        .id(domain.getId())
        .paymentMethod(domain.getPaymentMethod())
        .amount(domain.getAmount())
        .status(domain.getStatus())
        .createdAt(domain.getCreatedAt())
        .build();
  }
}

