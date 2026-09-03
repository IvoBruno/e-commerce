package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.ProductUseCase;
import com.ecommerce.infrastructure.adapters.input.rest.dto.ProductDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductUseCase productUseCase;

  @GetMapping
  public ResponseEntity<List<ProductDTO>> findAll() {
    List<ProductDTO> list = productUseCase.findAll().stream()
        .map(ProductDTO::fromDomain)
        .toList();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(ProductDTO.fromDomain(productUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
    ProductDTO saved = ProductDTO.fromDomain(productUseCase.save(productDTO.toDomain()));
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    ProductDTO updated = ProductDTO.fromDomain(productUseCase.update(id, productDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}

