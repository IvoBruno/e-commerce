package com.ecommerce.application.ports.output;

import com.ecommerce.domain.model.User;

public interface TokenServicePort {
  String generateToken(User user);

  String extractEmail(String token);

  boolean validateToken(String token, String email);
}

