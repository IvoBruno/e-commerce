package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
  List<Product> findAll();

  PageResult<Product> findWithFilters(
      Long categoryId,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  Optional<Product> findById(Long id);

  Product save(Product product);

  void deleteById(Long id);

  boolean existsById(Long id);
}
