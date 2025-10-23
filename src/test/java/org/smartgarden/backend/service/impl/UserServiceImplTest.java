package org.smartgarden.backend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.entity.UserRole;
import org.smartgarden.backend.exception.ConflictException;
import org.smartgarden.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void createUser_withValidData_shouldCreateUser() {
        String username = "newuser";
        String password = "password123";
        String role = "USER";
        String encodedPassword = "encodedPassword123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.builder()
                    .id(1L)
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .build();
        });

        User result = userService.createUser(username, password, role);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(UserRole.USER, result.getRole());
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_withExistingUsername_shouldThrowConflictException() {
        String username = "existinguser";
        String password = "password123";
        String role = "USER";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.createUser(username, password, role)
        );

        assertTrue(exception.getMessage().contains("already exists"));
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_withAdminRole_shouldCreateAdminUser() {
        String username = "admin";
        String password = "adminpass";
        String role = "ADMIN";
        String encodedPassword = "encodedAdminPass";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.builder()
                    .id(2L)
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .build();
        });

        User result = userService.createUser(username, password, role);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(UserRole.ADMIN, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void listUsers_shouldReturnAllUsers() {
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .password("pass1")
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("admin")
                .password("pass2")
                .role(UserRole.ADMIN)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.listUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("admin", result.get(1).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void listUsers_withNoUsers_shouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.listUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }
}

