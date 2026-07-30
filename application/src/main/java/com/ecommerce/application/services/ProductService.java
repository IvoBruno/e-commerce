package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.ProductDTO;
import com.ecommerce.application.entities.Product;
import com.ecommerce.application.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
   private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
       this.productRepository = productRepository;
    }

    @Transactional
    public List<ProductDTO> findAll() {
       return productRepository
               .findAll()
               .stream()
               .map(ProductDTO::new)
               .toList();
    }

    @Transactional
   public ProductDTO findById(Long id) {
       return new ProductDTO(productRepository.getReferenceById(id));
   }

   @Transactional
   public ProductDTO save(Product product) {
       return new ProductDTO(productRepository.save(product));
   }

   @Transactional
   public ProductDTO update(Product product) {
       return new ProductDTO(productRepository.save(product));
   }

   @Transactional
   public void delete(Product product) {
       productRepository.delete(product);
   }
}
