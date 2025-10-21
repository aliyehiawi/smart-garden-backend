package org.smartgarden.backend.controller;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.SensorData;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.DeviceRepository;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.SensorDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/gardens")
@RequiredArgsConstructor
public class SensorHistoryController {
    private final GardenRepository gardenRepository;
    private final DeviceRepository deviceRepository;
    private final SensorDataRepository sensorDataRepository;

    @GetMapping("/{id}/sensor-data")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<SensorData>> history(@PathVariable("id") Long gardenId,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "50") int size) {
        Garden garden = gardenRepository.findById(gardenId).orElseThrow(() -> new NotFoundException("Garden not found"));
        Device device = deviceRepository.findByGarden_Id(garden.getId()).stream().findFirst().orElseThrow(() -> new NotFoundException("No device for garden"));
        LocalDateTime start = from != null ? from : LocalDateTime.now().minusDays(1);
        LocalDateTime end = to != null ? to : LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sensorDataRepository.findByDeviceAndTimestampBetween(device, start, end, pageable));
    }
}


