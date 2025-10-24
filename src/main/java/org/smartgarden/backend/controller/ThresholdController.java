package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.mapper.ThresholdMapper;
import org.smartgarden.backend.service.ThresholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/thresholds")
@RequiredArgsConstructor
public class ThresholdController {
    private final ThresholdService thresholdService;
    private final ThresholdMapper thresholdMapper;

    @PutMapping("/{gardenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ThresholdDtos.ThresholdResponse> update(
            @PathVariable Long gardenId,
            @Valid @RequestBody ThresholdDtos.ThresholdRequest req) {
        req.setGardenId(gardenId);
        return ResponseEntity.ok(thresholdMapper.toResponse(thresholdService.upsertThreshold(req)));
    }

    @GetMapping("/{gardenId}")
    public ResponseEntity<List<ThresholdDtos.ThresholdResponse>> getByGarden(@PathVariable Long gardenId) {
        return ResponseEntity.ok(thresholdService.getThresholdsByGarden(gardenId).stream()
                .map(thresholdMapper::toResponse)
                .toList());
    }

    @GetMapping("/{gardenId}/{sensorType}")
    public ResponseEntity<ThresholdDtos.ThresholdResponse> getByGardenAndSensor(
            @PathVariable Long gardenId,
            @PathVariable String sensorType) {
        return ResponseEntity.ok(thresholdMapper.toResponse(
                thresholdService.getThresholdByGardenAndSensor(gardenId, sensorType)));
    }
}


