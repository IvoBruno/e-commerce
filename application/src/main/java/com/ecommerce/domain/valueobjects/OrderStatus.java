package com.ecommerce.domain.valueobjects;

public enum OrderStatus {
  WAITING_PAYMENT,
  PAID,
  SHIPPED,
  DELIVERED,
  CANCELLED;

  public static OrderStatus fromString(String status) {
    if (status == null) {
      return WAITING_PAYMENT;
    }
    try {
      return OrderStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      return WAITING_PAYMENT;
    }
  }
}

