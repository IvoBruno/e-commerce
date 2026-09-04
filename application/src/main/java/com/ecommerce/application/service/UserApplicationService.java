package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.UserUseCase;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService implements UserUseCase {
  private final UserRepositoryPort userRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResult<User> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    return userRepositoryPort.findWithFilters(search, page, size, sortBy, sortDirection);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public User save(User user) {
    if (user.getCreatedAt() == null) {
      user.setCreatedAt(LocalDateTime.now());
    }
    return userRepositoryPort.save(user);
  }

  @Override
  public User update(Long id, User user) {
    User existing = findById(id);
    existing.setName(user.getName());
    existing.setEmail(user.getEmail());
    existing.setPassword(user.getPassword());
    existing.setCpf(user.getCpf());
    return userRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!userRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userRepositoryPort.deleteById(id);
  }
}
