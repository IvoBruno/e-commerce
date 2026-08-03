package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.OrderDTO;
import com.ecommerce.application.entities.Order;
import com.ecommerce.application.repositories.OrderRepository;
import com.ecommerce.application.repositories.PaymentRepository;
import com.ecommerce.application.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
   private final OrderRepository orderRepository;
   private final UserRepository userRepository;
   private final PaymentRepository paymentRepository;
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
   public OrderDTO create(OrderDTO order) {
      return new OrderDTO(orderRepository.save(Order
         .builder()
         .user(userRepository.getReferenceById(order.user_id()))
         .totalAmount(order.totalAmount())
         .createdAt(order.createdAt())
         .status(order.status())
         .payment(paymentRepository.getReferenceById(order.payment_id()))
         .build()
      ));
   }

   @Transactional
   public OrderDTO update(Long id, OrderDTO order) {
      Order existingOrder = orderRepository.getReferenceById(id);
      existingOrder.setTotalAmount(order.totalAmount());
      existingOrder.setStatus(order.status());
      existingOrder.setPayment(paymentRepository.getReferenceById(order.payment_id()));
      return new OrderDTO(orderRepository.save(existingOrder));
   }

   @Transactional
   public void delete(Long id) {
      orderRepository.deleteById(id);
   }
}
