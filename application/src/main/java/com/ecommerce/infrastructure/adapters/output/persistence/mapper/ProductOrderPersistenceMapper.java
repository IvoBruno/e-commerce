package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.ProductOrder;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.OrderJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductOrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderPersistenceMapper {

  public ProductOrder toDomain(ProductOrderJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    Long productId = entity.getProduct() != null ? entity.getProduct().getId() : null;
    Long orderId = entity.getOrder() != null ? entity.getOrder().getId() : null;
    return ProductOrder.builder()
        .id(entity.getId())
        .productId(productId)
        .orderId(orderId)
        .quantity(entity.getQuantity())
        .unityPrice(entity.getUnityPrice())
        .build();
  }

  public ProductOrderJpaEntity toJpaEntity(ProductOrder domain) {
    if (domain == null) {
      return null;
    }
    ProductJpaEntity product = null;
    if (domain.getProductId() != null) {
      product = ProductJpaEntity.builder().id(domain.getProductId()).build();
    }
    OrderJpaEntity order = null;
    if (domain.getOrderId() != null) {
      order = OrderJpaEntity.builder().id(domain.getOrderId()).build();
    }
    return ProductOrderJpaEntity.builder()
        .id(domain.getId())
        .product(product)
        .order(order)
        .quantity(domain.getQuantity())
        .unityPrice(domain.getUnityPrice())
        .build();
  }
}

