package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.Category;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryPersistenceMapper {

  public Category toDomain(CategoryJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return Category.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .build();
  }

  public CategoryJpaEntity toJpaEntity(Category domain) {
    if (domain == null) {
      return null;
    }
    return CategoryJpaEntity.builder()
        .id(domain.getId())
        .name(domain.getName())
        .description(domain.getDescription())
        .build();
  }
}

