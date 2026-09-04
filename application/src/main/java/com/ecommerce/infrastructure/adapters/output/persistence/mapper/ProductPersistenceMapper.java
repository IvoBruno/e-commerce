package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.Product;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.CategoryJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

  public Product toDomain(ProductJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    Long categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
    return Product.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .price(entity.getPrice())
        .quantity(entity.getQuantity())
        .createdAt(entity.getCreatedAt())
        .categoryId(categoryId)
        .build();
  }

  public ProductJpaEntity toJpaEntity(Product domain) {
    if (domain == null) {
      return null;
    }
    CategoryJpaEntity category = null;
    if (domain.getCategoryId() != null) {
      category = CategoryJpaEntity.builder().id(domain.getCategoryId()).build();
    }
    return ProductJpaEntity.builder()
        .id(domain.getId())
        .name(domain.getName())
        .description(domain.getDescription())
        .price(domain.getPrice())
        .quantity(domain.getQuantity())
        .createdAt(domain.getCreatedAt())
        .category(category)
        .build();
  }
}

