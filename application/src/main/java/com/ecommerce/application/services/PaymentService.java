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
   public PaymentDTO save(Payment payment) {
      return new PaymentDTO(paymentRepository.save(payment));
   }

   @Transactional
   public PaymentDTO update(Payment payment) {
      return new PaymentDTO(paymentRepository.save(payment));
   }

   @Transactional
   public void delete(Payment payment) {
      paymentRepository.delete(payment);
   }
}
