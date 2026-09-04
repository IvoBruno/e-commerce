package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.ProductOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductOrderDTO(
    Long id,

    @NotNull(message = "Product ID is required")
    Long product_id,

    @NotNull(message = "Order ID is required")
    Long order_id,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    Integer quantity,

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
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
