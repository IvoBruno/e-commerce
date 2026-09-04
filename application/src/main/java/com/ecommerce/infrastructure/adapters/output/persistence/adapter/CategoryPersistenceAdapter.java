package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.CategoryRepositoryPort;
import com.ecommerce.domain.model.Category;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.CategoryJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataCategoryRepository;
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
  public PageResult<Category> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    String cleanSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : "id";
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, cleanSortBy));

    Specification<CategoryJpaEntity> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isBlank()) {
        String pattern = "%" + search.toLowerCase().trim() + "%";
        predicates.add(cb.like(cb.lower(root.get("name")), pattern));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Page<CategoryJpaEntity> entityPage = repository.findAll(spec, pageable);
    List<Category> categories = entityPage.getContent().stream()
        .map(mapper::toDomain)
        .toList();

    return new PageResult<>(
        categories,
        entityPage.getNumber(),
        entityPage.getSize(),
        entityPage.getTotalElements(),
        entityPage.getTotalPages(),
        entityPage.isFirst(),
        entityPage.isLast()
    );
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
