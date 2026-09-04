package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Category;
import com.ecommerce.domain.model.PageResult;
import java.util.List;

public interface CategoryUseCase {
  List<Category> findAll();

  PageResult<Category> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  Category findById(Long id);

  Category create(Category category);

  Category update(Long id, Category category);

  void delete(Long id);
}
