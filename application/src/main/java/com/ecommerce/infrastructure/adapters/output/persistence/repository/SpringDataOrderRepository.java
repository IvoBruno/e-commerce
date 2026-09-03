package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderJpaEntity, Long> {
}
