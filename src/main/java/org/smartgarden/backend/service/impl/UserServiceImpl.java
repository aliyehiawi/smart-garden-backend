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

/**
 * Service implementation for user management operations.
 * 
 * <p>This service handles user creation, password encryption,
 * and user listing operations. All passwords are encrypted
 * using BCrypt before storage.
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user with encrypted password.
     * 
     * <p>This method validates that the username is unique,
     * encrypts the password using BCrypt, and persists the
     * user to the database.
     * 
     * @param username the unique username
     * @param password the plain text password (will be encrypted)
     * @param role the user role (ADMIN or USER)
     * @return the created User entity
     * @throws ConflictException if the username already exists
     */
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

    /**
     * Retrieves all users from the database.
     * 
     * @return a list of all User entities
     */
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}


