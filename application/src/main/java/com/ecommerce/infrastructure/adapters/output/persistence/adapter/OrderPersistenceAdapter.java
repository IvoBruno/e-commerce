package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.OrderRepositoryPort;
import com.ecommerce.domain.model.Order;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.OrderJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.OrderPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataOrderRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {
  private final SpringDataOrderRepository repository;
  private final OrderPersistenceMapper mapper;

  @Override
  public List<Order> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Order> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Order save(Order order) {
    OrderJpaEntity entity = mapper.toJpaEntity(order);
    OrderJpaEntity saved = repository.save(entity);
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

