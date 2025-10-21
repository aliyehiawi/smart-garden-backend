package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.SensorDtos;
import org.smartgarden.backend.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @PostMapping("/{deviceId}/data")
    public ResponseEntity<Void> ingest(@PathVariable String deviceId, @Valid @RequestBody SensorDtos.SensorDataRequest req) {
        sensorService.saveReading(deviceId, req);
        return ResponseEntity.ok().build();
    }
}


