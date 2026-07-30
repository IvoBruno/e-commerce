package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
