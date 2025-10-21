package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.UserDtos;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.mapper.UserMapper;
import org.smartgarden.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDtos.UserResponse> create(@Valid @RequestBody UserDtos.UserRequest request) {
        User user = userService.createUser(request.getUsername(), request.getPassword(), request.getRole());
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDtos.UserResponse>> list() {
        return ResponseEntity.ok(userService.listUsers().stream().map(userMapper::toResponse).toList());
    }
}


