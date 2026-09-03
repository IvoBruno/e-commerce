package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
  List<User> findAll();
  Optional<User> findById(Long id);
  User save(User user);
  void deleteById(Long id);
  boolean existsById(Long id);
}

