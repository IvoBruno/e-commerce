package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, Long> {
}
