package org.smartgarden.backend.controller;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.entity.Alert;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.repository.AlertRepository;
import org.smartgarden.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Alert>> list(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(alertRepository.findByUserOrderByTimestampDesc(user));
    }
}


