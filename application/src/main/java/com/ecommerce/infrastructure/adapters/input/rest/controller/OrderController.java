package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.OrderUseCase;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.input.rest.dto.OrderDTO;
import jakarta.validation.Valid;
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
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderUseCase orderUseCase;

  @GetMapping
  public ResponseEntity<PageResult<OrderDTO>> findAll(
      @RequestParam(required = false) Long userId,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    PageResult<OrderDTO> result = orderUseCase.findWithFilters(userId, status, page, size, sortBy, sortDirection)
        .map(OrderDTO::fromDomain);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(OrderDTO.fromDomain(orderUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO orderDTO) {
    OrderDTO created = OrderDTO.fromDomain(orderUseCase.create(orderDTO.toDomain()));
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody OrderDTO orderDTO
  ) {
    OrderDTO updated = OrderDTO.fromDomain(orderUseCase.update(id, orderDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}
