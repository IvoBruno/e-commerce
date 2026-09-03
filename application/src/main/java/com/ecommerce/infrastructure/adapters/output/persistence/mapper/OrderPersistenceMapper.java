package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.Order;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.OrderJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.PaymentJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceMapper {

  public Order toDomain(OrderJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    Long userId = entity.getUser() != null ? entity.getUser().getId() : null;
    Long paymentId = entity.getPayment() != null ? entity.getPayment().getId() : null;
    return Order.builder()
        .id(entity.getId())
        .userId(userId)
        .totalAmount(entity.getTotalAmount())
        .createdAt(entity.getCreatedAt())
        .status(entity.getStatus())
        .paymentId(paymentId)
        .build();
  }

  public OrderJpaEntity toJpaEntity(Order domain) {
    if (domain == null) {
      return null;
    }
    UserJpaEntity user = null;
    if (domain.getUserId() != null) {
      user = UserJpaEntity.builder().id(domain.getUserId()).build();
    }
    PaymentJpaEntity payment = null;
    if (domain.getPaymentId() != null) {
      payment = PaymentJpaEntity.builder().id(domain.getPaymentId()).build();
    }
    return OrderJpaEntity.builder()
        .id(domain.getId())
        .user(user)
        .totalAmount(domain.getTotalAmount())
        .createdAt(domain.getCreatedAt())
        .status(domain.getStatus())
        .payment(payment)
        .build();
  }
}

