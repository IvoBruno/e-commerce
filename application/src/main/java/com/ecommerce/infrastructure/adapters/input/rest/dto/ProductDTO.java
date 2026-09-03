package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Long quantity,
    LocalDate createdAt,
    Long category_id
) {
  public static ProductDTO fromDomain(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductDTO(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getQuantity(),
        product.getCreatedAt(),
        product.getCategoryId()
    );
  }

  public Product toDomain() {
    return Product.builder()
        .id(id)
        .name(name)
        .description(description)
        .price(price)
        .quantity(quantity)
        .createdAt(createdAt)
        .categoryId(category_id)
        .build();
  }
}

