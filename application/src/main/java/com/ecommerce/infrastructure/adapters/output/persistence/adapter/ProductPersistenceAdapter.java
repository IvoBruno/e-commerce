package com.ecommerce.infrastructure.adapters.output.persistence.adapter;

import com.ecommerce.application.ports.output.ProductRepositoryPort;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.Product;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.ProductJpaEntity;
import com.ecommerce.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.ecommerce.infrastructure.adapters.output.persistence.repository.SpringDataProductRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
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
  public PageResult<Product> findWithFilters(
      Long categoryId,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    String cleanSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : "id";
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(direction, cleanSortBy));

    Specification<ProductJpaEntity> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (categoryId != null) {
        predicates.add(cb.equal(root.get("categoryId"), categoryId));
      }
      if (minPrice != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
      }
      if (maxPrice != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
      }
      if (search != null && !search.isBlank()) {
        String pattern = "%" + search.toLowerCase().trim() + "%";
        Predicate nameMatch = cb.like(cb.lower(root.get("name")), pattern);
        Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
        predicates.add(cb.or(nameMatch, descMatch));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Page<ProductJpaEntity> entityPage = repository.findAll(spec, pageable);
    List<Product> products = entityPage.getContent().stream()
        .map(mapper::toDomain)
        .toList();

    return new PageResult<>(
        products,
        entityPage.getNumber(),
        entityPage.getSize(),
        entityPage.getTotalElements(),
        entityPage.getTotalPages(),
        entityPage.isFirst(),
        entityPage.isLast()
    );
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
