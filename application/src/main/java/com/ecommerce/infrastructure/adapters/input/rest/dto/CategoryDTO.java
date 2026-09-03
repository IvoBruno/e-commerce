package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Category;

public record CategoryDTO(
    Long id,
    String name,
    String description
) {
  public static CategoryDTO fromDomain(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
  }

  public Category toDomain() {
    return Category.builder()
        .id(id)
        .name(name)
        .description(description)
        .build();
  }
}

