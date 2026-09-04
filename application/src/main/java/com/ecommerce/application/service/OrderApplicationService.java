package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.OrderUseCase;
import com.ecommerce.application.ports.output.OrderRepositoryPort;
import com.ecommerce.application.ports.output.PaymentRepositoryPort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.valueobjects.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderApplicationService implements OrderUseCase {
  private final OrderRepositoryPort orderRepositoryPort;
  private final UserRepositoryPort userRepositoryPort;
  private final PaymentRepositoryPort paymentRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<Order> findAll() {
    return orderRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResult<Order> findWithFilters(
      Long userId,
      String status,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    return orderRepositoryPort.findWithFilters(userId, status, page, size, sortBy, sortDirection);
  }

  @Override
  @Transactional(readOnly = true)
  public Order findById(Long id) {
    return orderRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
  }

  @Override
  public Order create(Order order) {
    if (order.getUserId() == null || !userRepositoryPort.existsById(order.getUserId())) {
      throw new ResourceNotFoundException("User not found with id: " + order.getUserId());
    }
    if (order.getPaymentId() != null && !paymentRepositoryPort.existsById(order.getPaymentId())) {
      throw new ResourceNotFoundException("Payment not found with id: " + order.getPaymentId());
    }
    if (order.getCreatedAt() == null) {
      order.setCreatedAt(LocalDateTime.now());
    }
    if (order.getStatus() == null) {
      order.setStatus(OrderStatus.WAITING_PAYMENT.name());
    }
    if (order.getTotalAmount() == null) {
      order.setTotalAmount(BigDecimal.ZERO);
    }
    return orderRepositoryPort.save(order);
  }

  @Override
  public Order update(Long id, Order order) {
    Order existing = findById(id);
    if (order.getPaymentId() != null && !paymentRepositoryPort.existsById(order.getPaymentId())) {
      throw new ResourceNotFoundException("Payment not found with id: " + order.getPaymentId());
    }
    existing.setTotalAmount(order.getTotalAmount());
    existing.setStatus(order.getStatus());
    existing.setPaymentId(order.getPaymentId());
    return orderRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!orderRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("Order not found with id: " + id);
    }
    orderRepositoryPort.deleteById(id);
  }
}
