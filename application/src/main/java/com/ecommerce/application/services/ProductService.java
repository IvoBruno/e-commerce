package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.ProductDTO;
import com.ecommerce.application.entities.Product;
import com.ecommerce.application.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
   private final ProductRepository productRepository;

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
   public ProductDTO save(ProductDTO product) {
       return new ProductDTO(productRepository.save(Product.builder()
               .name(product.name())
               .description(product.description())
               .price(product.price())
               .quantity(product.quantity())
               .build()));
   }

   @Transactional
   public ProductDTO update(ProductDTO product) {
       return new ProductDTO(productRepository.save(Product.builder()
               .id(product.id())
               .name(product.name())
               .description(product.description())
               .price(product.price())
               .quantity(product.quantity())
               .build()));
   }

   @Transactional
   public void delete(ProductDTO product) {
       productRepository.deleteById(product.id());
   }
}
