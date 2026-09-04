package com.ecommerce.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ecommerce.domain.exception.InsufficientStockException;
import com.ecommerce.domain.model.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

  @Test
  @DisplayName("Should deduct stock successfully when sufficient quantity exists")
  void shouldDeductStockSuccessfully() {
    Product product = Product.builder()
        .id(1L)
        .name("Mechanical Keyboard")
        .price(new BigDecimal("120.00"))
        .quantity(10L)
        .build();

    product.deductStock(3L);

    assertEquals(7L, product.getQuantity());
  }

  @Test
  @DisplayName("Should throw InsufficientStockException when requested amount exceeds available stock")
  void shouldThrowWhenStockInsufficient() {
    Product product = Product.builder()
        .id(1L)
        .name("Mechanical Keyboard")
        .price(new BigDecimal("120.00"))
        .quantity(2L)
        .build();

    assertThrows(InsufficientStockException.class, () -> product.deductStock(5L));
  }

  @Test
  @DisplayName("Should add stock successfully")
  void shouldAddStockSuccessfully() {
    Product product = Product.builder()
        .id(1L)
        .name("Mechanical Keyboard")
        .quantity(5L)
        .build();

    product.addStock(10L);

    assertEquals(15L, product.getQuantity());
  }
}

