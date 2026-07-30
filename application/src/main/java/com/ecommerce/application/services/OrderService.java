package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.OrderDTO;
import com.ecommerce.application.entities.Order;
import com.ecommerce.application.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
   private final OrderRepository orderRepository;

   @Transactional
   public List<OrderDTO> findAll() {
      return orderRepository.findAll()
              .stream()
              .map(OrderDTO::new)
              .toList();
   }

   @Transactional
   public OrderDTO findById(Long id) {
      return new  OrderDTO(orderRepository.getReferenceById(id));
   }

   @Transactional
   public OrderDTO create(Order order) {
      return new OrderDTO(orderRepository.save(order));
   }

   @Transactional
   public OrderDTO update(Order order) {
      return new OrderDTO(orderRepository.save(order));
   }

   @Transactional
   public void delete(Order order) {
      orderRepository.delete(order);
   }
}
