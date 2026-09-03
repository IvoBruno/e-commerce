package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.ProductOrderUseCase;
import com.ecommerce.application.ports.output.OrderRepositoryPort;
import com.ecommerce.application.ports.output.ProductOrderRepositoryPort;
import com.ecommerce.application.ports.output.ProductRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.ProductOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderApplicationService implements ProductOrderUseCase {
  private final ProductOrderRepositoryPort productOrderRepositoryPort;
  private final ProductRepositoryPort productRepositoryPort;
  private final OrderRepositoryPort orderRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<ProductOrder> findAll() {
    return productOrderRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public ProductOrder findById(Long id) {
    return productOrderRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ProductOrder not found with id: " + id));
  }

  @Override
  public ProductOrder save(ProductOrder productOrder) {
    if (productOrder.getProductId() == null || !productRepositoryPort.existsById(productOrder.getProductId())) {
      throw new ResourceNotFoundException("Product not found with id: " + productOrder.getProductId());
    }
    if (productOrder.getOrderId() == null || !orderRepositoryPort.existsById(productOrder.getOrderId())) {
      throw new ResourceNotFoundException("Order not found with id: " + productOrder.getOrderId());
    }
    return productOrderRepositoryPort.save(productOrder);
  }

  @Override
  public ProductOrder update(Long id, ProductOrder productOrder) {
    ProductOrder existing = findById(id);
    if (productOrder.getProductId() != null && !productRepositoryPort.existsById(productOrder.getProductId())) {
      throw new ResourceNotFoundException("Product not found with id: " + productOrder.getProductId());
    }
    if (productOrder.getOrderId() != null && !orderRepositoryPort.existsById(productOrder.getOrderId())) {
      throw new ResourceNotFoundException("Order not found with id: " + productOrder.getOrderId());
    }
    existing.setProductId(productOrder.getProductId());
    existing.setOrderId(productOrder.getOrderId());
    existing.setQuantity(productOrder.getQuantity());
    existing.setUnityPrice(productOrder.getUnityPrice());
    return productOrderRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!productOrderRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("ProductOrder not found with id: " + id);
    }
    productOrderRepositoryPort.deleteById(id);
  }
}

