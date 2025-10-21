package org.smartgarden.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.entity.UserRole;
import org.smartgarden.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
                log.info("seed-admin username=admin");
            }
        };
    }
}


