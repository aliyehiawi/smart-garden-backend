package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.dto.AuthDtos;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.repository.UserRepository;
import org.smartgarden.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for authentication operations.
 * Handles user login and JWT token generation.
 * 
 * <p>All endpoints in this controller are publicly accessible
 * and do not require authentication.
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * <p>This endpoint validates user credentials using Spring Security's
     * authentication mechanism and generates a JWT token containing the
     * user's username and role.
     * 
     * @param request the login request containing username and password
     * @return ResponseEntity containing the JWT token in the response body
     * @throws BadCredentialsException if the username or password is incorrect
     * @throws RuntimeException if any other error occurs during authentication
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDtos.LoginResponse> login(
            @Valid @RequestBody AuthDtos.LoginRequest request) {
        log.debug("Login attempt for username: {}", request.getUsername());
        
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
            
            User user = userRepository.findByUsername(auth.getName()).orElseThrow();
            log.info("User authenticated successfully - username: {}, role: {}", 
                    user.getUsername(), user.getRole());
            
            String token = jwtUtil.generateToken(
                    user.getUsername(), 
                    Map.of("role", user.getRole().name()));
            
            AuthDtos.LoginResponse resp = new AuthDtos.LoginResponse();
            resp.setToken(token);
            return ResponseEntity.ok(resp);
            
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt - invalid credentials for username: {}", 
                    request.getUsername());
            throw e;
        } catch (Exception e) {
            log.error("Login error for username: {} - {}", 
                    request.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
}


