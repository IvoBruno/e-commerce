package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.PaymentUseCase;
import com.ecommerce.infrastructure.adapters.input.rest.dto.PaymentDTO;
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
@RequestMapping("/api/payments")
public class PaymentController {
  private final PaymentUseCase paymentUseCase;

  @GetMapping
  public ResponseEntity<List<PaymentDTO>> findAll() {
    List<PaymentDTO> list = paymentUseCase.findAll().stream()
        .map(PaymentDTO::fromDomain)
        .toList();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(PaymentDTO.fromDomain(paymentUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO paymentDTO) {
    PaymentDTO saved = PaymentDTO.fromDomain(paymentUseCase.save(paymentDTO.toDomain()));
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PaymentDTO> update(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
    PaymentDTO updated = PaymentDTO.fromDomain(paymentUseCase.update(id, paymentDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    paymentUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}

