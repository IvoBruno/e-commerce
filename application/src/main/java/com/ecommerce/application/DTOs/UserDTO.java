package com.ecommerce.application.DTOs;

import com.ecommerce.application.entities.User;

public record UserDTO(
        Long id,
        String name,
        String email,
        String password,
        String cpf) {
   public UserDTO(User  entity){
      this(
              entity.getId(),
              entity.getName(),
              entity.getEmail(),
              entity.getPassword(),
              entity.getCpf()
      );
   }
}
