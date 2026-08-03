package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.ProductDTO;
import com.ecommerce.application.entities.Product;
import com.ecommerce.application.repositories.CategoryRepository;
import com.ecommerce.application.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;

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
               .createdAt(LocalDate.now())
               .category(categoryRepository.findById(product.category_id())
                       .orElseThrow(() -> new RuntimeException("Category not found")))
               .build()));
   }

   @Transactional
   public ProductDTO update(Long id, ProductDTO product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.name());
        existingProduct.setDescription(product.description());
        existingProduct.setPrice(product.price());
        existingProduct.setQuantity(product.quantity());
        existingProduct.setCategory(categoryRepository.findById(product.category_id())
            .orElseThrow(() -> new RuntimeException("Category not found")));
       return new ProductDTO(productRepository.save(existingProduct));
   }

   @Transactional
   public void delete(Long id) {
       productRepository.deleteById(id);
   }
}
