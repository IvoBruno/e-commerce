package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.PageResult;
import java.util.List;

public interface OrderUseCase {
  List<Order> findAll();

  PageResult<Order> findWithFilters(
      Long userId,
      String status,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  Order findById(Long id);

  Order create(Order order);

  Order update(Long id, Order order);

  void delete(Long id);
}
