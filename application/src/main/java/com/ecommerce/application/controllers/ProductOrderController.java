package com.ecommerce.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.application.DTOs.ProductOrderDTO;
import com.ecommerce.application.services.ProductOrderService;
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
@RequestMapping("/api/product-orders")
public class ProductOrderController {
    private final ProductOrderService productOrderService;
    
    @GetMapping
    public ResponseEntity<List<ProductOrderDTO>> findAll() {
        return ResponseEntity.ok(productOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productOrderService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<ProductOrderDTO> save(@RequestBody ProductOrderDTO productOrder) {
        return ResponseEntity.ok(productOrderService.save(productOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> update(@PathVariable Long id, @RequestBody ProductOrderDTO productOrder) {
        return ResponseEntity.ok(productOrderService.update(id, productOrder));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
