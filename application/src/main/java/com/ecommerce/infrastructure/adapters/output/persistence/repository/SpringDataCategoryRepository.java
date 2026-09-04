package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataCategoryRepository
    extends JpaRepository<CategoryJpaEntity, Long>, JpaSpecificationExecutor<CategoryJpaEntity> {
}
