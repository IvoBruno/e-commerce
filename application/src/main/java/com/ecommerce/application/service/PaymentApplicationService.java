package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.PaymentUseCase;
import com.ecommerce.application.ports.output.PaymentRepositoryPort;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.Payment;
import com.ecommerce.domain.valueobjects.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentApplicationService implements PaymentUseCase {
  private final PaymentRepositoryPort paymentRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public List<Payment> findAll() {
    return paymentRepositoryPort.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Payment findById(Long id) {
    return paymentRepositoryPort.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
  }

  @Override
  public Payment save(Payment payment) {
    if (payment.getCreatedAt() == null) {
      payment.setCreatedAt(LocalDateTime.now());
    }
    if (payment.getStatus() == null) {
      payment.setStatus(PaymentStatus.PENDING.name());
    }
    return paymentRepositoryPort.save(payment);
  }

  @Override
  public Payment update(Long id, Payment payment) {
    Payment existing = findById(id);
    existing.setPaymentMethod(payment.getPaymentMethod());
    existing.setAmount(payment.getAmount());
    existing.setStatus(payment.getStatus());
    return paymentRepositoryPort.save(existing);
  }

  @Override
  public void delete(Long id) {
    if (!paymentRepositoryPort.existsById(id)) {
      throw new ResourceNotFoundException("Payment not found with id: " + id);
    }
    paymentRepositoryPort.deleteById(id);
  }
}

