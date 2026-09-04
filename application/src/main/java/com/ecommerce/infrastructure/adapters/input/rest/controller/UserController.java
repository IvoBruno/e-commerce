package com.ecommerce.infrastructure.adapters.input.rest.controller;

import com.ecommerce.application.ports.input.UserUseCase;
import com.ecommerce.domain.model.PageResult;
import com.ecommerce.infrastructure.adapters.input.rest.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserUseCase userUseCase;

  @GetMapping
  public ResponseEntity<PageResult<UserDTO>> findAll(
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection
  ) {
    PageResult<UserDTO> result = userUseCase.findWithFilters(search, page, size, sortBy, sortDirection)
        .map(UserDTO::fromDomain);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(UserDTO.fromDomain(userUseCase.findById(id)));
  }

  @PostMapping
  public ResponseEntity<UserDTO> save(@Valid @RequestBody UserDTO userDTO) {
    UserDTO saved = UserDTO.fromDomain(userUseCase.save(userDTO.toDomain()));
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody UserDTO userDTO
  ) {
    UserDTO updated = UserDTO.fromDomain(userUseCase.update(id, userDTO.toDomain()));
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }
}
