package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.Category;

public record CategoryDTO(
        Long id,
        String name,
        String description
) {
   public CategoryDTO(Category entity){
      this(
              entity.getId(),
              entity.getName(),
              entity.getDescription()
      );
   }
}
