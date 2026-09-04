package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.ProductOrderRepositoryPort;
import com.ecommerce.domain.model.ProductOrder;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductOrderJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.ProductOrderPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataProductOrderRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOrderPersistenceAdapter implements ProductOrderRepositoryPort {
  private final SpringDataProductOrderRepository repository;
  private final ProductOrderPersistenceMapper mapper;

  @Override
  public List<ProductOrder> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<ProductOrder> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public ProductOrder save(ProductOrder productOrder) {
    ProductOrderJpaEntity entity = mapper.toJpaEntity(productOrder);
    ProductOrderJpaEntity saved = repository.save(entity);
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

