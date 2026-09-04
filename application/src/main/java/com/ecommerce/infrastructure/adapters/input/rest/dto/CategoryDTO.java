package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
    Long id,

    @NotBlank(message = "Category name is required")
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
