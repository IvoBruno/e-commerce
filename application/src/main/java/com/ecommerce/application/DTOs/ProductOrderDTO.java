package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.ProductOrder;

import java.math.BigDecimal;

public record ProductOrderDTO(
        Long id,
        Long product_id,
        Long order_id,
        Integer quantity,
        BigDecimal unity_price
) {
   public ProductOrderDTO(ProductOrder entity){
      this(
              entity.getId(),
              entity.getProduct().getId(),
              entity.getOrder().getId(),
              entity.getQuantity(),
              entity.getUnityPrice()
      );
   }
}
