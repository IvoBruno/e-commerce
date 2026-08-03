package com.ecommerce.application.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ecommerce.application.DTOs.UserDTO;
import com.ecommerce.application.entities.User;
import com.ecommerce.application.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public List<UserDTO> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    @Transactional
    public UserDTO findById(Long id) {
        return new UserDTO(userRepository.getReferenceById(id));
    }

    @Transactional
    public UserDTO save(UserDTO user) {
        return new UserDTO(userRepository.save(User.builder()
                .name(user.name())
                .email(user.email())
                .password(user.password())
                .cpf(user.cpf())
                .createdAt(LocalDateTime.now())
                .build()));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO user) {
        User existingUser = userRepository.getReferenceById(id);
        existingUser.setName(user.name());
        existingUser.setEmail(user.email());
        existingUser.setPassword(user.password());
        return new UserDTO(userRepository.save(existingUser));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
