package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.ProductOrder;
import java.util.List;

public interface ProductOrderUseCase {
  List<ProductOrder> findAll();
  ProductOrder findById(Long id);
  ProductOrder save(ProductOrder productOrder);
  ProductOrder update(Long id, ProductOrder productOrder);
  void delete(Long id);
}

