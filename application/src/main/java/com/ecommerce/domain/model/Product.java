package com.ecommerce.domain.model;

import com.ecommerce.domain.exception.InsufficientStockException;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Long quantity;
  private LocalDate createdAt;
  private Long categoryId;

  public void deductStock(long amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Deduction quantity must be greater than zero");
    }
    if (this.quantity == null || this.quantity < amount) {
      throw new InsufficientStockException("Insufficient stock for product " + this.id + ". Available: " + this.quantity + ", Requested: " + amount);
    }
    this.quantity -= amount;
  }

  public void addStock(long amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Stock quantity to add must be greater than zero");
    }
    this.quantity = (this.quantity == null ? 0 : this.quantity) + amount;
  }
}

