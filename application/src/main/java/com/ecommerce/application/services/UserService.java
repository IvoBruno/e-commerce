package com.ecommerce.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.application.DTOs.UserDTO;
import com.ecommerce.application.entities.User;
import com.ecommerce.application.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
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
    public UserDTO save(User user) {
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(User user) {
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }
}
