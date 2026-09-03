package com.ecommerce.domain.model;

import com.ecommerce.domain.valueobjects.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
  private Long id;
  private String paymentMethod;
  private BigDecimal amount;
  private String status;
  private LocalDateTime createdAt;

  public void markCompleted() {
    this.status = PaymentStatus.COMPLETED.name();
  }

  public void markFailed() {
    this.status = PaymentStatus.FAILED.name();
  }
}

