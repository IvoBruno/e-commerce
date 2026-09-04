package com.ecommerce.domain.model;

import java.math.BigDecimal;
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
public class ProductOrder {
  private Long id;
  private Long productId;
  private Long orderId;
  private Integer quantity;
  private BigDecimal unityPrice;

  public BigDecimal calculateSubtotal() {
    if (unityPrice == null || quantity == null) {
      return BigDecimal.ZERO;
    }
    return unityPrice.multiply(BigDecimal.valueOf(quantity));
  }
}

