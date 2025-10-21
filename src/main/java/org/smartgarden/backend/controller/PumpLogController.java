package org.smartgarden.backend.controller;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.PumpLog;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.PumpLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pump/logs")
@RequiredArgsConstructor
public class PumpLogController {
    private final GardenRepository gardenRepository;
    private final PumpLogRepository pumpLogRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PumpLog>> list(@RequestParam Long gardenId) {
        Garden garden = gardenRepository.findById(gardenId).orElseThrow(() -> new NotFoundException("Garden not found"));
        return ResponseEntity.ok(pumpLogRepository.findByGardenOrderByStartedAtDesc(garden));
    }
}


