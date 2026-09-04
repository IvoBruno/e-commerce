package com.ecommerce.infrastructure.adapters.input.rest.dto;

import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(
    Long id,

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    String password,

    @Pattern(
        regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)",
        message = "Invalid CPF format (expected 11 digits or 000.000.000-00)"
    )
    String cpf,

    UserRole role
) {
  public UserDTO(Long id, String name, String email, String password, String cpf) {
    this(id, name, email, password, cpf, UserRole.ROLE_CLIENT);
  }

  public static UserDTO fromDomain(User user) {
    if (user == null) {
      return null;
    }
    return new UserDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        null, // Never expose password in responses
        user.getCpf(),
        user.getRole() != null ? user.getRole() : UserRole.ROLE_CLIENT
    );
  }

  public User toDomain() {
    return User.builder()
        .id(id)
        .name(name)
        .email(email)
        .password(password)
        .cpf(cpf)
        .role(role != null ? role : UserRole.ROLE_CLIENT)
        .build();
  }
}
