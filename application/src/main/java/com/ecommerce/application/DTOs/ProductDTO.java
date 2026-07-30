package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.Product;

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
   public ProductDTO(Product entity){
      this(
              entity.getId(),
              entity.getName(),
              entity.getDescription(),
              entity.getPrice(),
              entity.getQuantity(),
              entity.getCreatedAt(),
              entity.getCategory().getId()
      );
   }
}
