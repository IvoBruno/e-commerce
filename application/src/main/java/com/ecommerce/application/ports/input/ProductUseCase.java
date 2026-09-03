package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Product;
import java.util.List;

public interface ProductUseCase {
  List<Product> findAll();
  Product findById(Long id);
  Product save(Product product);
  Product update(Long id, Product product);
  void delete(Long id);
}

