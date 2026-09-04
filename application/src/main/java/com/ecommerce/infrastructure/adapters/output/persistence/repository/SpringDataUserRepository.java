package com.ecommerce.infrastructure.adapters.output.persistence.repository;

import com.ecommerce.infrastructure.adapters.output.persistence.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataUserRepository
    extends JpaRepository<UserJpaEntity, Long>, JpaSpecificationExecutor<UserJpaEntity> {

  Optional<UserJpaEntity> findByEmail(String email);
}
