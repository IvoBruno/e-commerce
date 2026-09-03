package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.ProductOrder;
import java.math.BigDecimal;

public record ProductOrderDTO(
    Long id,
    Long product_id,
    Long order_id,
    Integer quantity,
    BigDecimal unity_price
) {
  public static ProductOrderDTO fromDomain(ProductOrder productOrder) {
    if (productOrder == null) {
      return null;
    }
    return new ProductOrderDTO(
        productOrder.getId(),
        productOrder.getProductId(),
        productOrder.getOrderId(),
        productOrder.getQuantity(),
        productOrder.getUnityPrice()
    );
  }

  public ProductOrder toDomain() {
    return ProductOrder.builder()
        .id(id)
        .productId(product_id)
        .orderId(order_id)
        .quantity(quantity)
        .unityPrice(unity_price)
        .build();
  }
}

