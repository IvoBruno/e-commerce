package com.ecommerce.application.service;

import com.ecommerce.application.ports.input.AuthUseCase;
import com.ecommerce.application.ports.input.UserUseCase;
import com.ecommerce.application.ports.output.PasswordEncoderPort;
import com.ecommerce.application.ports.output.TokenServicePort;
import com.ecommerce.application.ports.output.UserRepositoryPort;
import com.ecommerce.domain.exception.DomainException;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.model.UserRole;
import com.ecommerce.infrastructure.adapters.input.rest.dto.AuthResponseDTO;
import com.ecommerce.infrastructure.adapters.input.rest.dto.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthApplicationService implements AuthUseCase {
  private final UserRepositoryPort userRepositoryPort;
  private final PasswordEncoderPort passwordEncoderPort;
  private final TokenServicePort tokenServicePort;
  private final UserUseCase userUseCase;

  @Override
  @Transactional(readOnly = true)
  public AuthResponseDTO login(LoginRequestDTO request) {
    User user = userRepositoryPort.findByEmail(request.email())
        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

    if (!passwordEncoderPort.matches(request.password(), user.getPassword())) {
      throw new BadCredentialsException("Invalid email or password");
    }

    String role = user.getRole() != null ? user.getRole().name() : UserRole.ROLE_CLIENT.name();
    String token = tokenServicePort.generateToken(user);
    return AuthResponseDTO.bearer(token, user.getId(), user.getName(), user.getEmail(), role);
  }

  @Override
  public User register(User user) {
    if (user.getEmail() != null && userRepositoryPort.findByEmail(user.getEmail()).isPresent()) {
      throw new DomainException("Email is already registered: " + user.getEmail());
    }
    if (user.getRole() == null) {
      user.setRole(UserRole.ROLE_CLIENT);
    }
    return userUseCase.save(user);
  }
}
