package org.smartgarden.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security UserDetailsService implementation using JPA.
 * 
 * <p>This service loads user details from the database for Spring Security
 * authentication. It's used by the AuthenticationManager to validate
 * credentials during login.
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads user details by username for Spring Security authentication.
     * 
     * <p>This method queries the database for a user with the given username
     * and converts it to a Spring Security UserDetails object containing
     * the username, encrypted password, and role authority.
     * 
     * @param username the username to search for
     * @return UserDetails containing user authentication information
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found during authentication: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}


