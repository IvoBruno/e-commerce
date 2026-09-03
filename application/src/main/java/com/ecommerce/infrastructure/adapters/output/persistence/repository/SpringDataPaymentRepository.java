package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentJpaEntity, Long> {
}
