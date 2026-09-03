package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.ProductRepositoryPort;
import com.ecommerce.domain.model.Product;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {
  private final SpringDataProductRepository repository;
  private final ProductPersistenceMapper mapper;

  @Override
  public List<Product> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Product> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Product save(Product product) {
    ProductJpaEntity entity = mapper.toJpaEntity(product);
    ProductJpaEntity saved = repository.save(entity);
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

