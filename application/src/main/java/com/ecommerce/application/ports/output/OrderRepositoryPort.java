package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
  List<Order> findAll();
  Optional<Order> findById(Long id);
  Order save(Order order);
  void deleteById(Long id);
  boolean existsById(Long id);
}

