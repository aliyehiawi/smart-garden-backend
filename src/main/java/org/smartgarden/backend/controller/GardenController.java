package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.GardenDtos;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.mapper.GardenMapper;
import org.smartgarden.backend.service.GardenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gardens")
@RequiredArgsConstructor
public class GardenController {
    private final GardenService gardenService;
    private final GardenMapper gardenMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GardenDtos.GardenResponse> create(@Valid @RequestBody GardenDtos.GardenRequest req) {
        Garden g = gardenService.createGarden(req.getName(), req.getDescription(), req.getLocation());
        return ResponseEntity.ok(gardenMapper.toResponse(g));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<GardenDtos.GardenResponse>> list(Authentication authentication) {
        List<Garden> gardens = gardenService.listGardensForUser(authentication.getName());
        return ResponseEntity.ok(gardens.stream().map(gardenMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<GardenDtos.GardenResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(gardenMapper.toResponse(gardenService.getGarden(id)));
    }
}


