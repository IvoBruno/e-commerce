package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductUseCase {
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

  Product findById(Long id);

  Product save(Product product);

  Product update(Long id, Product product);

  void delete(Long id);
}
