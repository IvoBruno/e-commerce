package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Category;
import java.util.List;

public interface CategoryUseCase {
  List<Category> findAll();
  Category findById(Long id);
  Category create(Category category);
  Category update(Long id, Category category);
  void delete(Long id);
}

