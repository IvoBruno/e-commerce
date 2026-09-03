package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.Payment;
import java.util.List;

public interface PaymentUseCase {
  List<Payment> findAll();
  Payment findById(Long id);
  Payment save(Payment payment);
  Payment update(Long id, Payment payment);
  void delete(Long id);
}

