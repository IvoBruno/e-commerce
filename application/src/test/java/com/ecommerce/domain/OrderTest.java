package com.ecommerce.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.ProductOrder;
import com.ecommerce.domain.valueobjects.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("Should correctly calculate total amount from order items")
  void shouldCalculateTotalAmount() {
    ProductOrder item1 = ProductOrder.builder()
        .productId(1L)
        .quantity(2)
        .unityPrice(new BigDecimal("50.00"))
        .build();

    ProductOrder item2 = ProductOrder.builder()
        .productId(2L)
        .quantity(1)
        .unityPrice(new BigDecimal("30.00"))
        .build();

    Order order = Order.builder()
        .id(1L)
        .items(List.of(item1, item2))
        .build();

    order.calculateTotal();

    assertEquals(new BigDecimal("130.00"), order.getTotalAmount());
  }

  @Test
  @DisplayName("Should update status on markAsPaid and cancel")
  void shouldUpdateStatusOnTransitions() {
    Order order = Order.builder()
        .id(1L)
        .status(OrderStatus.WAITING_PAYMENT.name())
        .build();

    order.markAsPaid();
    assertEquals(OrderStatus.PAID.name(), order.getStatus());

    order.cancel();
    assertEquals(OrderStatus.CANCELLED.name(), order.getStatus());
  }
}

