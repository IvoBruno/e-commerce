package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.ProductOrderDTO;
import com.ecommerce.application.entities.ProductOrder;
import com.ecommerce.application.repositories.ProductOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderService {
   private final ProductOrderRepository productOrderRepository;
   public ProductOrderService(ProductOrderRepository productOrderRepository) {
      this.productOrderRepository = productOrderRepository;
   }

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
   public ProductOrderDTO save(ProductOrder productOrder) {
      return new ProductOrderDTO(productOrderRepository.save(productOrder));
   }

   @Transactional
   public ProductOrderDTO update (ProductOrder productOrder) {
      return new ProductOrderDTO(productOrderRepository.save(productOrder));
   }

   @Transactional
   public void delete (Long id) {
      productOrderRepository.deleteById(id);
   }
}
