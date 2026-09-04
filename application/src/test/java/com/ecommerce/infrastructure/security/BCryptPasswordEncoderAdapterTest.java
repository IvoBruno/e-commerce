package com.ecommerce.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BCryptPasswordEncoderAdapterTest {

  private BCryptPasswordEncoderAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new BCryptPasswordEncoderAdapter(new BCryptPasswordEncoder());
  }

  @Test
  @DisplayName("Should encode password with BCrypt format ($2a$)")
  void shouldEncodePasswordWithBCrypt() {
    String raw = "mySecurePassword123";
    String encoded = adapter.encode(raw);

    assertNotNull(encoded);
    assertTrue(encoded.startsWith("$2a$"), "Encoded password must be a valid BCrypt hash");
  }

  @Test
  @DisplayName("Should match correct raw password against encoded hash")
  void shouldMatchCorrectPassword() {
    String raw = "mySecurePassword123";
    String encoded = adapter.encode(raw);

    assertTrue(adapter.matches(raw, encoded));
    assertFalse(adapter.matches("wrongPassword", encoded));
  }

  @Test
  @DisplayName("Should return false when raw or encoded password is null")
  void shouldHandleNullInputsSafely() {
    assertFalse(adapter.matches(null, "$2a$10$xyz"));
    assertFalse(adapter.matches("raw", null));
  }
}

