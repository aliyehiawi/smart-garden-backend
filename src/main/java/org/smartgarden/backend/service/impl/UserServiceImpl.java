package org.smartgarden.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.entity.UserRole;
import org.smartgarden.backend.exception.ConflictException;
import org.smartgarden.backend.repository.UserRepository;
import org.smartgarden.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("User creation failed - username already exists: {}", username);
            throw new ConflictException("Username already exists: " + username);
        }
        
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(UserRole.valueOf(role))
                .build();
        
        userRepository.save(user);
        log.info("User created successfully - username: {}, role: {}", username, role);
        return user;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}


