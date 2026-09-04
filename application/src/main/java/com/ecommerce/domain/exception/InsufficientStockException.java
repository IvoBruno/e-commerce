package com.ecommerce.domain.exception;

public class InsufficientStockException extends DomainException {
  public InsufficientStockException(String message) {
    super(message);
  }
}

