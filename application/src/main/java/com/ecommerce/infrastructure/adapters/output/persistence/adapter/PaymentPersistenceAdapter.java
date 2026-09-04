package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.PaymentRepositoryPort;
import com.ecommerce.domain.model.Payment;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.PaymentJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.PaymentPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataPaymentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentRepositoryPort {
  private final SpringDataPaymentRepository repository;
  private final PaymentPersistenceMapper mapper;

  @Override
  public List<Payment> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Payment> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Payment save(Payment payment) {
    PaymentJpaEntity entity = mapper.toJpaEntity(payment);
    PaymentJpaEntity saved = repository.save(entity);
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

