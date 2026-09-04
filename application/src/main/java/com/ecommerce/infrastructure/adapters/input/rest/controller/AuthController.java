package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.AuthUseCase;
import com.ecommerce.domain.model.User;
import com.ecommerce.infrastructure.adapters.input.rest.dto.AuthResponseDTO;
import com.ecommerce.infrastructure.adapters.input.rest.dto.LoginRequestDTO;
import com.ecommerce.infrastructure.adapters.input.rest.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthUseCase authUseCase;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    AuthResponseDTO response = authUseCase.login(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO request) {
    User saved = authUseCase.register(request.toDomain());
    return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromDomain(saved));
  }
}

