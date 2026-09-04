package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.ProductUseCase;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.input.rest.dto.ProductDTO;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductUseCase productUseCase;

  @GetMapping
  public ResponseEntity<PageResult<ProductDTO>> findAll(
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    PageResult<ProductDTO> result = productUseCase.findWithFilters(
        categoryId, minPrice, maxPrice, search, page, size, sortBy, sortDirection
    ).map(ProductDTO::fromDomain);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(ProductDTO.fromDomain(productUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDTO) {
    ProductDTO saved = ProductDTO.fromDomain(productUseCase.save(productDTO.toDomain()));
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody ProductDTO productDTO
  ) {
    ProductDTO updated = ProductDTO.fromDomain(productUseCase.update(id, productDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}
