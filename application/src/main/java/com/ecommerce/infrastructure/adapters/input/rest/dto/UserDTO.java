package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.User;

public record UserDTO(
    Long id,
    String name,
    String email,
    String password,
    String cpf
) {
  public static UserDTO fromDomain(User user) {
    if (user == null) {
      return null;
    }
    return new UserDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getPassword(),
        user.getCpf()
    );
  }

  public User toDomain() {
    return User.builder()
        .id(id)
        .name(name)
        .email(email)
        .password(password)
        .cpf(cpf)
        .build();
  }
}

