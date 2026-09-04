package com.ecommerce.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.application.ports.output.PasswordEncoderPort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.application.service.UserApplicationService;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

  @Mock
  private UserRepositoryPort userRepositoryPort;

  @Mock
  private PasswordEncoderPort passwordEncoderPort;

  @InjectMocks
  private UserApplicationService userApplicationService;

  @Test
  @DisplayName("Should encode password and save user")
  void shouldEncodePasswordAndSaveUser() {
    User rawUser = User.builder()
        .name("Alice")
        .email("alice@example.com")
        .password("plainTextPass")
        .cpf("123.456.789-00")
        .build();

    when(passwordEncoderPort.encode("plainTextPass")).thenReturn("$2a$10$encodedHashString");
    when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User saved = userApplicationService.save(rawUser);

    assertNotNull(saved);
    assertEquals("$2a$10$encodedHashString", saved.getPassword());
    assertEquals(UserRole.ROLE_CLIENT, saved.getRole());
    assertNotNull(saved.getCreatedAt());
    verify(passwordEncoderPort).encode("plainTextPass");
    verify(userRepositoryPort).save(rawUser);
  }

  @Test
  @DisplayName("Should update user and encode new password when provided")
  void shouldUpdateUserAndEncodeNewPassword() {
    User existing = User.builder()
        .id(1L)
        .name("Old Name")
        .email("old@example.com")
        .password("$2a$10$oldHash")
        .build();

    User updateRequest = User.builder()
        .name("New Name")
        .email("new@example.com")
        .password("newSecretPass")
        .build();

    when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
    when(passwordEncoderPort.encode("newSecretPass")).thenReturn("$2a$10$newEncodedHash");
    when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User updated = userApplicationService.update(1L, updateRequest);

    assertEquals("New Name", updated.getName());
    assertEquals("new@example.com", updated.getEmail());
    assertEquals("$2a$10$newEncodedHash", updated.getPassword());
    verify(passwordEncoderPort).encode("newSecretPass");
  }

  @Test
  @DisplayName("Should preserve existing password on update when password is not provided")
  void shouldPreservePasswordOnUpdateWhenNull() {
    User existing = User.builder()
        .id(1L)
        .name("Old Name")
        .email("old@example.com")
        .password("$2a$10$existingHash")
        .build();

    User updateRequest = User.builder()
        .name("New Name")
        .email("new@example.com")
        .password(null)
        .build();

    when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
    when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User updated = userApplicationService.update(1L, updateRequest);

    assertEquals("$2a$10$existingHash", updated.getPassword());
    verify(passwordEncoderPort, never()).encode(any());
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
  void shouldThrowWhenDeletingNonExistentUser() {
    when(userRepositoryPort.existsById(99L)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> userApplicationService.delete(99L));
  }

  @Test
  @DisplayName("Should delegate findWithFilters to repository port")
  void shouldDelegateFindWithFilters() {
    PageResult<User> mockPage = PageResult.of(List.of(User.builder().id(1L).build()), 0, 10, 1);
    when(userRepositoryPort.findWithFilters("alice", 0, 10, "id", "asc")).thenReturn(mockPage);

    PageResult<User> result = userApplicationService.findWithFilters("alice", 0, 10, "id", "asc");

    assertEquals(1, result.totalElements());
    verify(userRepositoryPort).findWithFilters("alice", 0, 10, "id", "asc");
  }
}

