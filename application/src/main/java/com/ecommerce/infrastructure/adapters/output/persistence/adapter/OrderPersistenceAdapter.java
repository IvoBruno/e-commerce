package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.OrderRepositoryPort;
import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.OrderJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.OrderPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataOrderRepository;
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
  public PageResult<Order> findWithFilters(
      Long userId,
      String status,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    String cleanSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : "id";
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, cleanSortBy));

    Specification<OrderJpaEntity> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (userId != null) {
        predicates.add(cb.equal(root.get("userId"), userId));
      }
      if (status != null && !status.isBlank()) {
        predicates.add(cb.equal(cb.upper(root.get("status")), status.trim().toUpperCase()));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Page<OrderJpaEntity> entityPage = repository.findAll(spec, pageable);
    List<Order> orders = entityPage.getContent().stream()
        .map(mapper::toDomain)
        .toList();

    return new PageResult<>(
        orders,
        entityPage.getNumber(),
        entityPage.getSize(),
        entityPage.getTotalElements(),
        entityPage.getTotalPages(),
        entityPage.isFirst(),
        entityPage.isLast()
    );
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
