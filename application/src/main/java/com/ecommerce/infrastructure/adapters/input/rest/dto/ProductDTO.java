package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDTO(
    Long id,

    @NotBlank(message = "Product name is required")
    String name,

    String description,

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    BigDecimal price,

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    Long quantity,

    LocalDate createdAt,

    @NotNull(message = "Category ID is required")
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
