package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductOrderRepository extends JpaRepository<ProductOrderJpaEntity, Long> {
}
