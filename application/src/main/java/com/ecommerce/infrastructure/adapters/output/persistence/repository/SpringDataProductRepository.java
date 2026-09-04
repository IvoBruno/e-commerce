package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataProductRepository
    extends JpaRepository<ProductJpaEntity, Long>, JpaSpecificationExecutor<ProductJpaEntity> {
}
