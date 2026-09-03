package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.OrderUseCase;
import com.ecommerce.infrastructure.adapters.input.rest.dto.OrderDTO;
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
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderUseCase orderUseCase;

  @GetMapping
  public ResponseEntity<List<OrderDTO>> findAll() {
    List<OrderDTO> list = orderUseCase.findAll().stream()
        .map(OrderDTO::fromDomain)
        .toList();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(OrderDTO.fromDomain(orderUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO orderDTO) {
    OrderDTO created = OrderDTO.fromDomain(orderUseCase.create(orderDTO.toDomain()));
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
    OrderDTO updated = OrderDTO.fromDomain(orderUseCase.update(id, orderDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}

