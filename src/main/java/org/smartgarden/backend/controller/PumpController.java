package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.PumpDtos;
import org.smartgarden.backend.entity.InitiatedBy;
import org.smartgarden.backend.service.PumpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gardens/{id}/pump")
@RequiredArgsConstructor
public class PumpController {
    private final PumpService pumpService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> start(@PathVariable("id") Long gardenId, @Valid @RequestBody(required = false) PumpDtos.PumpStartRequest req) {
        Integer duration = req != null ? req.getDurationSeconds() : null;
        pumpService.startPump(gardenId, duration, InitiatedBy.ADMIN);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> stop(@PathVariable("id") Long gardenId) {
        pumpService.stopPump(gardenId, InitiatedBy.ADMIN);
        return ResponseEntity.ok().build();
    }
}


