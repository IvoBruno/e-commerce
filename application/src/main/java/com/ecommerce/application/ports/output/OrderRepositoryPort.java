package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.PageResult;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
  List<Order> findAll();

  PageResult<Order> findWithFilters(
      Long userId,
      String status,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  Optional<Order> findById(Long id);

  Order save(Order order);

  void deleteById(Long id);

  boolean existsById(Long id);
}
