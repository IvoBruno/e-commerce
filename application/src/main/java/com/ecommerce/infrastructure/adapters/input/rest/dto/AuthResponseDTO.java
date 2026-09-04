package com.ecommerce.infrastructure.adapters.input.rest.dto;

public record AuthResponseDTO(
    String token,
    String type,
    Long userId,
    String name,
    String email,
    String role
) {
  public static AuthResponseDTO bearer(String token, Long userId, String name, String email, String role) {
    return new AuthResponseDTO(token, "Bearer", userId, name, email, role);
  }
}
