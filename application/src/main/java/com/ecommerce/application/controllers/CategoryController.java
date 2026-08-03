package com.ecommerce.application.controllers;

import com.ecommerce.application.DTOs.CategoryDTO;
import com.ecommerce.application.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
   private final CategoryService categoryService;


   @GetMapping
   public ResponseEntity<List<CategoryDTO>> getAllCategories() {
      return ResponseEntity.ok(categoryService.findAll());
   }

   @GetMapping("/{id}")
   public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
      return ResponseEntity.ok(categoryService.getById(id));
   }

   @PostMapping
   public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO category) {
      CategoryDTO newCategory = categoryService.create(category);
      return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
      categoryService.delete(id);
      return ResponseEntity.noContent().build();
   }

   @PutMapping("/{id}")
   public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO category) {
      return ResponseEntity.ok(categoryService.update(id, category));
   }
}
