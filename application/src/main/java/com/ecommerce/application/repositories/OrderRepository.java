package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
