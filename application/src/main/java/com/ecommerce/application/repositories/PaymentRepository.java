package com.ecommerce.application.repositories;

import com.ecommerce.application.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
