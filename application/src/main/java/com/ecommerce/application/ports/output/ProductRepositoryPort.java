package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
  List<Product> findAll();
  Optional<Product> findById(Long id);
  Product save(Product product);
  void deleteById(Long id);
  boolean existsById(Long id);
}

