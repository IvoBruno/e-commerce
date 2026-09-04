package com.ecommerce.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.application.ports.input.UserUseCase;
import com.ecommerce.application.ports.output.PasswordEncoderPort;
import com.ecommerce.application.ports.output.TokenServicePort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.application.service.AuthApplicationService;
import com.ecommerce.domain.exception.DomainException;
import com.ecommerce.domain.model.User;
import com.ecommerce.infrastructure.adapters.input.rest.dto.AuthResponseDTO;
import com.ecommerce.infrastructure.adapters.input.rest.dto.LoginRequestDTO;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

  @Mock
  private UserRepositoryPort userRepositoryPort;

  @Mock
  private PasswordEncoderPort passwordEncoderPort;

  @Mock
  private TokenServicePort tokenServicePort;

  @Mock
  private UserUseCase userUseCase;

  @InjectMocks
  private AuthApplicationService authApplicationService;

  @Test
  @DisplayName("Should login successfully with valid credentials")
  void shouldLoginSuccessfully() {
    User user = User.builder()
        .id(10L)
        .name("Alice Security")
        .email("alice@example.com")
        .password("$2a$10$hashedPassword")
        .build();

    LoginRequestDTO request = new LoginRequestDTO("alice@example.com", "plainPassword");

    when(userRepositoryPort.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoderPort.matches("plainPassword", "$2a$10$hashedPassword")).thenReturn(true);
    when(tokenServicePort.generateToken(user)).thenReturn("mocked.jwt.token");

    AuthResponseDTO response = authApplicationService.login(request);

    assertNotNull(response);
    assertEquals("mocked.jwt.token", response.token());
    assertEquals("Bearer", response.type());
    assertEquals(10L, response.userId());
    assertEquals("alice@example.com", response.email());
    assertEquals("ROLE_CLIENT", response.role());
  }

  @Test
  @DisplayName("Should throw BadCredentialsException when user not found")
  void shouldThrowWhenUserNotFound() {
    LoginRequestDTO request = new LoginRequestDTO("unknown@example.com", "plainPassword");
    when(userRepositoryPort.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

    assertThrows(BadCredentialsException.class, () -> authApplicationService.login(request));
  }

  @Test
  @DisplayName("Should throw BadCredentialsException when password does not match")
  void shouldThrowWhenPasswordMismatch() {
    User user = User.builder()
        .id(10L)
        .email("alice@example.com")
        .password("$2a$10$hashedPassword")
        .build();

    LoginRequestDTO request = new LoginRequestDTO("alice@example.com", "wrongPassword");
    when(userRepositoryPort.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoderPort.matches("wrongPassword", "$2a$10$hashedPassword")).thenReturn(false);

    assertThrows(BadCredentialsException.class, () -> authApplicationService.login(request));
  }

  @Test
  @DisplayName("Should register new user successfully")
  void shouldRegisterNewUser() {
    User newUser = User.builder()
        .name("Charlie")
        .email("charlie@example.com")
        .password("plainSecret")
        .build();

    when(userRepositoryPort.findByEmail("charlie@example.com")).thenReturn(Optional.empty());
    when(userUseCase.save(newUser)).thenReturn(newUser);

    User registered = authApplicationService.register(newUser);

    assertNotNull(registered);
    verify(userUseCase).save(newUser);
  }

  @Test
  @DisplayName("Should throw DomainException when registering with duplicate email")
  void shouldThrowWhenRegisteringDuplicateEmail() {
    User existing = User.builder().id(1L).email("existing@example.com").build();
    User newUser = User.builder().email("existing@example.com").build();

    when(userRepositoryPort.findByEmail("existing@example.com")).thenReturn(Optional.of(existing));

    assertThrows(DomainException.class, () -> authApplicationService.register(newUser));
  }
}

