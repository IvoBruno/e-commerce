package com.ecommerce.infrastructure.adapters.output.persistence.mapper;

import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import com.ecommerce.infrastructure.adapters.output.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

  public User toDomain(UserJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return User.builder()
        .id(entity.getId())
        .name(entity.getName())
        .email(entity.getEmail())
        .password(entity.getPassword())
        .cpf(entity.getCpf())
        .role(entity.getRole() != null ? entity.getRole() : UserRole.ROLE_CLIENT)
        .createdAt(entity.getCreatedAt())
        .build();
  }

  public UserJpaEntity toJpaEntity(User domain) {
    if (domain == null) {
      return null;
    }
    return UserJpaEntity.builder()
        .id(domain.getId())
        .name(domain.getName())
        .email(domain.getEmail())
        .password(domain.getPassword())
        .cpf(domain.getCpf())
        .role(domain.getRole() != null ? domain.getRole() : UserRole.ROLE_CLIENT)
        .createdAt(domain.getCreatedAt())
        .build();
  }
}
