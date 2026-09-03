package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Order;
import java.util.List;

public interface OrderUseCase {
  List<Order> findAll();
  Order findById(Long id);
  Order create(Order order);
  Order update(Long id, Order order);
  void delete(Long id);
}

