package com.ecommerce.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.application.DTOs.PaymentDTO;
import com.ecommerce.application.services.PaymentService;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> finddAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));    
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO payment) {
        return ResponseEntity.ok(paymentService.save(payment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable Long id, @RequestBody PaymentDTO payment) {
        return ResponseEntity.ok(paymentService.update(id, payment));
    }
    
}
