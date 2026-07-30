package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
