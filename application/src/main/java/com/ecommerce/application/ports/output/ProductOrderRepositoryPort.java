package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.ProductOrder;
import java.util.List;
import java.util.Optional;

public interface ProductOrderRepositoryPort {
  List<ProductOrder> findAll();
  Optional<ProductOrder> findById(Long id);
  ProductOrder save(ProductOrder productOrder);
  void deleteById(Long id);
  boolean existsById(Long id);
}

