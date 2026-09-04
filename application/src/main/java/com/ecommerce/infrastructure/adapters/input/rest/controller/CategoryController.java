package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.CategoryUseCase;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.input.rest.dto.CategoryDTO;
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
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryUseCase categoryUseCase;

  @GetMapping
  public ResponseEntity<PageResult<CategoryDTO>> getAllCategories(
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    PageResult<CategoryDTO> result = categoryUseCase.findWithFilters(search, page, size, sortBy, sortDirection)
        .map(CategoryDTO::fromDomain);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
    return ResponseEntity.ok(CategoryDTO.fromDomain(categoryUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
    CategoryDTO created = CategoryDTO.fromDomain(categoryUseCase.create(categoryDTO.toDomain()));
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody CategoryDTO categoryDTO
  ) {
    CategoryDTO updated = CategoryDTO.fromDomain(categoryUseCase.update(id, categoryDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
    categoryUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}
