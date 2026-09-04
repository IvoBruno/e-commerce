package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.Category;
import com.ecommerce.domain.model.PageResult;
import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
  List<Category> findAll();

  PageResult<Category> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  Optional<Category> findById(Long id);

  Category save(Category category);

  void deleteById(Long id);

  boolean existsById(Long id);
}
