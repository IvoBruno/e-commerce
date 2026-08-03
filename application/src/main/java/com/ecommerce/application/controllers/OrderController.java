package com.ecommerce.application.controllers;

import com.ecommerce.application.DTOs.OrderDTO;
import com.ecommerce.application.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
   private final OrderService orderService;

   @GetMapping
   public ResponseEntity<List<OrderDTO>> findAll() {
      return ResponseEntity.ok(orderService.findAll());
   }
   
   @GetMapping("/{id}")
   public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
      return ResponseEntity.ok(orderService.findById(id));
   }

   @PostMapping
   public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO orderDTO) {
       return ResponseEntity.ok(orderService.create(orderDTO));
   }

   @PutMapping("/{id}")
   public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO order) {     
      return ResponseEntity.ok(orderService.update(id, order));
   }
   
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {
      orderService.delete(id);
      return ResponseEntity.noContent().build();
   }
   
}
