package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
