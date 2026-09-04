package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.ProductUseCase;
import com.ecommerce.application.ports.output.CategoryRepositoryPort;
import com.ecommerce.application.ports.output.ProductRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductApplicationService implements ProductUseCase {
  private final ProductRepositoryPort productRepositoryPort;
  private final CategoryRepositoryPort categoryRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return productRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResult<Product> findWithFilters(
      Long categoryId,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    return productRepositoryPort.findWithFilters(categoryId, minPrice, maxPrice, search, page, size, sortBy, sortDirection);
  }

  @Override
  @Transactional(readOnly = true)
  public Product findById(Long id) {
    return productRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }

  @Override
  public Product save(Product product) {
    if (product.getCategoryId() != null && !categoryRepositoryPort.existsById(product.getCategoryId())) {
      throw new ResourceNotFoundException("Category not found with id: " + product.getCategoryId());
    }
    if (product.getCreatedAt() == null) {
      product.setCreatedAt(LocalDate.now());
    }
    return productRepositoryPort.save(product);
  }

  @Override
  public Product update(Long id, Product product) {
    Product existing = findById(id);
    if (product.getCategoryId() != null && !categoryRepositoryPort.existsById(product.getCategoryId())) {
      throw new ResourceNotFoundException("Category not found with id: " + product.getCategoryId());
    }
    existing.setName(product.getName());
    existing.setDescription(product.getDescription());
    existing.setPrice(product.getPrice());
    existing.setQuantity(product.getQuantity());
    existing.setCategoryId(product.getCategoryId());
    return productRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!productRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("Product not found with id: " + id);
    }
    productRepositoryPort.deleteById(id);
  }
}
