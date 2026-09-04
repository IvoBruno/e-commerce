package com.ecommerce.application.ports.output;

public interface PasswordEncoderPort {
  String encode(CharSequence rawPassword);

  boolean matches(CharSequence rawPassword, String encodedPassword);
}

