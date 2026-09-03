package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
  List<Payment> findAll();
  Optional<Payment> findById(Long id);
  Payment save(Payment payment);
  void deleteById(Long id);
  boolean existsById(Long id);
}

