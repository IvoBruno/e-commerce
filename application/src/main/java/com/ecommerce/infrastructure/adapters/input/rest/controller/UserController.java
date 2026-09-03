package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.UserUseCase;
import com.ecommerce.infrastructure.adapters.input.rest.dto.UserDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserUseCase userUseCase;

  @GetMapping
  public ResponseEntity<List<UserDTO>> findAll() {
    List<UserDTO> list = userUseCase.findAll().stream()
        .map(UserDTO::fromDomain)
        .toList();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(UserDTO.fromDomain(userUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
    UserDTO saved = UserDTO.fromDomain(userUseCase.save(userDTO.toDomain()));
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
    UserDTO updated = UserDTO.fromDomain(userUseCase.update(id, userDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}

