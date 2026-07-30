package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
