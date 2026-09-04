package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.User;
import java.util.List;

public interface UserUseCase {
  List<User> findAll();

  PageResult<User> findWithFilters(
      String search,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  User findById(Long id);

  User save(User user);

  User update(Long id, User user);

  void delete(Long id);
}
