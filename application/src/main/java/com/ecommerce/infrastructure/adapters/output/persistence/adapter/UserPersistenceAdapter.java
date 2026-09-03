package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.model.User;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.UserJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.UserPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataUserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {
  private final SpringDataUserRepository repository;
  private final UserPersistenceMapper mapper;

  @Override
  public List<User> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public Optional<User> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public User save(User user) {
    UserJpaEntity entity = mapper.toJpaEntity(user);
    UserJpaEntity saved = repository.save(entity);
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

