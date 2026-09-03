package com.ecommerce.domain.valueobjects;

public enum PaymentStatus {
  PENDING,
  COMPLETED,
  FAILED,
  CANCELLED;

  public static PaymentStatus fromString(String status) {
    if (status == null) {
      return PENDING;
    }
    try {
      return PaymentStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      return PENDING;
    }
  }
}

