package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.CategoryRepositoryPort;
import com.ecommerce.domain.model.Category;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.CategoryJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {
  private final SpringDataCategoryRepository repository;
  private final CategoryPersistenceMapper mapper;

  @Override
  public List<Category> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Category> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Category save(Category category) {
    CategoryJpaEntity entity = mapper.toJpaEntity(category);
    CategoryJpaEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return repository.existsById(id);
  }
}

