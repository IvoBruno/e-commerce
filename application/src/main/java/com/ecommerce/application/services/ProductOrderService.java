package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.ProductOrderDTO;
import com.ecommerce.application.entities.ProductOrder;
import com.ecommerce.application.exceptions.ResourceNotFoundException;
import com.ecommerce.application.repositories.OrderRepository;
import com.ecommerce.application.repositories.ProductRepository;
import com.ecommerce.application.repositories.ProductOrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductOrderService {
   private final ProductOrderRepository productOrderRepository;
   private final ProductRepository productRepository;
   private final OrderRepository orderRepository;


   @Transactional
   public List<ProductOrderDTO> findAll() {
      return productOrderRepository
              .findAll()
              .stream()
              .map(ProductOrderDTO::new)
              .toList();
   }

   @Transactional
   public ProductOrderDTO findById(Long id) {
      return new ProductOrderDTO(productOrderRepository.getReferenceById(id));
   }

   @Transactional
   public ProductOrderDTO save(ProductOrderDTO productOrder) {
      return new ProductOrderDTO(productOrderRepository.save(ProductOrder.builder()
              .product(productRepository.findById(productOrder.product_id()).orElseThrow(()-> new ResourceNotFoundException("Product not found")))
              .order(orderRepository.findById(productOrder.order_id()).orElseThrow(() -> new ResourceNotFoundException("Order not found")))
              .quantity(productOrder.quantity())
              .unityPrice(productOrder.unity_price())
              .build()));
   }

   @Transactional
   public ProductOrderDTO update (Long id, ProductOrderDTO productOrder) {
      ProductOrder existingProductOrder = productOrderRepository.getReferenceById(id);
      existingProductOrder.setProduct(productRepository.findById(productOrder.product_id()).orElseThrow(()-> new ResourceNotFoundException("Product not found")));
      existingProductOrder.setOrder(orderRepository.findById(productOrder.order_id()).orElseThrow(() -> new ResourceNotFoundException("Order not found")));
      existingProductOrder.setQuantity(productOrder.quantity());
      existingProductOrder.setUnityPrice(productOrder.unity_price());
      return new ProductOrderDTO(productOrderRepository.save(existingProductOrder));
   }

   @Transactional
   public void delete (Long id) {
      productOrderRepository.deleteById(id);
   }
}
