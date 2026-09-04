package com.ecommerce.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenAdapterTest {

  private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
  private static final long TEST_EXPIRATION_MS = 3600000; // 1 hour

  private JwtTokenAdapter jwtTokenAdapter;

  @BeforeEach
  void setUp() {
    jwtTokenAdapter = new JwtTokenAdapter(TEST_SECRET, TEST_EXPIRATION_MS);
  }

  @Test
  @DisplayName("Should generate valid JWT token with user email as subject and role claim")
  void shouldGenerateValidToken() {
    User user = User.builder()
        .id(1L)
        .name("Bob Admin")
        .email("bob@example.com")
        .role(UserRole.ROLE_ADMIN)
        .build();

    String token = jwtTokenAdapter.generateToken(user);

    assertNotNull(token);
    assertFalse(token.isBlank());
    assertEquals("bob@example.com", jwtTokenAdapter.extractEmail(token));
    assertEquals("ROLE_ADMIN", jwtTokenAdapter.extractRole(token));
  }

  @Test
  @DisplayName("Should validate token against matching email")
  void shouldValidateTokenAgainstMatchingEmail() {
    User user = User.builder()
        .id(1L)
        .name("Bob Test")
        .email("bob@example.com")
        .role(UserRole.ROLE_CLIENT)
        .build();

    String token = jwtTokenAdapter.generateToken(user);

    assertTrue(jwtTokenAdapter.validateToken(token, "bob@example.com"));
    assertFalse(jwtTokenAdapter.validateToken(token, "charlie@example.com"));
    assertEquals("ROLE_CLIENT", jwtTokenAdapter.extractRole(token));
  }

  @Test
  @DisplayName("Should reject malformed or expired token")
  void shouldRejectMalformedToken() {
    assertFalse(jwtTokenAdapter.validateToken("invalid.token.string", "bob@example.com"));
  }
}
