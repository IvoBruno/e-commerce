package com.ecommerce.application.ports.input;

import com.ecommerce.domain.model.User;
import com.ecommerce.infrastructure.adapters.input.rest.dto.AuthResponseDTO;
import com.ecommerce.infrastructure.adapters.input.rest.dto.LoginRequestDTO;

public interface AuthUseCase {
  AuthResponseDTO login(LoginRequestDTO request);

  User register(User user);
}

