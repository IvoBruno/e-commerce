package com.ecommerce.application.services;

import com.ecommerce.application.DTOs.PaymentDTO;
import com.ecommerce.application.entities.Payment;
import com.ecommerce.application.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
   private final PaymentRepository paymentRepository;

   @Transactional
   public List<PaymentDTO> findAll() {
      return paymentRepository
              .findAll()
              .stream()
              .map(PaymentDTO::new)
              .toList();
   }

   @Transactional
   public PaymentDTO findById(Long id) {
      return new PaymentDTO(paymentRepository.getReferenceById(id));
   }

   @Transactional
   public PaymentDTO save(PaymentDTO payment) {
      return new PaymentDTO(paymentRepository.save(Payment.builder()
              .paymentMethod(payment.paymentMethod())
              .amount(payment.amount())
              .status(payment.status())
              .createdAt(payment.createdAt())
              .build()));
   }

   @Transactional
   public PaymentDTO update(Long id, PaymentDTO payment) {
      Payment existingPayment = paymentRepository.getReferenceById(id);
      existingPayment.setStatus(payment.status());
      existingPayment.setPaymentMethod(payment.paymentMethod());
      return new PaymentDTO(paymentRepository.save(existingPayment));
   }

   @Transactional
   public void delete(Long id) {
      paymentRepository.deleteById(id);
   }
}
