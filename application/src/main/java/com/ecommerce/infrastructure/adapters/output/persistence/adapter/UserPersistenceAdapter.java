package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.User;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.UserJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.UserPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataUserRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
  public PageResult<User> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    String cleanSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : "id";
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, cleanSortBy));

    Specification<UserJpaEntity> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String pattern = "%" + search.toLowerCase().trim() + "%";
        Predicate nameMatch = cb.like(cb.lower(root.get("name")), pattern);
        Predicate emailMatch = cb.like(cb.lower(root.get("email")), pattern);
        predicates.add(cb.or(nameMatch, emailMatch));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Page<UserJpaEntity> entityPage = repository.findAll(spec, pageable);
    List<User> users = entityPage.getContent().stream()
        .map(mapper::toDomain)
        .toList();

    return new PageResult<>(
        users,
        entityPage.getNumber(),
        entityPage.getSize(),
        entityPage.getTotalElements(),
        entityPage.getTotalPages(),
        entityPage.isFirst(),
        entityPage.isLast()
    );
  }

  @Override
  public Optional<User> findById(Long id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return repository.findByEmail(email)
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
