package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.CategoryUseCase;
import com.ecommerce.application.ports.output.CategoryRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryApplicationService implements CategoryUseCase {
  private final CategoryRepositoryPort categoryRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<Category> findAll() {
    return categoryRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long id) {
    return categoryRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  @Override
  public Category create(Category category) {
    return categoryRepositoryPort.save(category);
  }

  @Override
  public Category update(Long id, Category category) {
    Category existing = findById(id);
    existing.setName(category.getName());
    existing.setDescription(category.getDescription());
    return categoryRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!categoryRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("Category not found with id: " + id);
    }
    categoryRepositoryPort.deleteById(id);
  }
}

