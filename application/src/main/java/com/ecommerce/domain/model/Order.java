package com.ecommerce.domain.model;

import com.ecommerce.domain.valueobjects.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Order {
  private Long id;
  private Long userId;
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private String status;
  private Long paymentId;
  @Builder.Default
  private List<ProductOrder> items = new ArrayList<>();

  public void calculateTotal() {
    if (items == null || items.isEmpty()) {
      this.totalAmount = BigDecimal.ZERO;
      return;
    }
    BigDecimal total = BigDecimal.ZERO;
    for (ProductOrder item : items) {
      if (item != null) {
        BigDecimal subtotal = item.calculateSubtotal();
        if (subtotal != null) {
          total = total.add(subtotal);
        }
      }
    }
    this.totalAmount = total;
  }

  public void markAsPaid() {
    this.status = OrderStatus.PAID.name();
  }

  public void cancel() {
    this.status = OrderStatus.CANCELLED.name();
  }
}

