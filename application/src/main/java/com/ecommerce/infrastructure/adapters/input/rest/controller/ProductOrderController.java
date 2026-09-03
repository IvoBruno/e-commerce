package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.ProductOrderUseCase;
import com.ecommerce.infrastructure.adapters.input.rest.dto.ProductOrderDTO;
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
@RequestMapping("/api/product-orders")
public class ProductOrderController {
  private final ProductOrderUseCase productOrderUseCase;

  @GetMapping
  public ResponseEntity<List<ProductOrderDTO>> findAll() {
    List<ProductOrderDTO> list = productOrderUseCase.findAll().stream()
        .map(ProductOrderDTO::fromDomain)
        .toList();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductOrderDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(ProductOrderDTO.fromDomain(productOrderUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<ProductOrderDTO> save(@RequestBody ProductOrderDTO productOrderDTO) {
    ProductOrderDTO saved = ProductOrderDTO.fromDomain(productOrderUseCase.save(productOrderDTO.toDomain()));
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductOrderDTO> update(@PathVariable Long id, @RequestBody ProductOrderDTO productOrderDTO) {
    ProductOrderDTO updated = ProductOrderDTO.fromDomain(productOrderUseCase.update(id, productOrderDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productOrderUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}

