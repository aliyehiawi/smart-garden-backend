package org.smartgarden.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.DeviceDtos;
import org.smartgarden.backend.entity.DeviceCommand;
import org.smartgarden.backend.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceDtos.DeviceResponse> register(@Valid @RequestBody DeviceDtos.DeviceRequest request) {
        return ResponseEntity.ok(deviceService.registerDevice(request));
    }

    @GetMapping("/{deviceId}/commands")
    public ResponseEntity<List<DeviceCommand>> pending(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getPendingCommands(deviceId));
    }

    @PostMapping("/{deviceId}/commands/{commandId}/ack")
    public ResponseEntity<Void> ack(@PathVariable String deviceId, @PathVariable Long commandId, @Valid @RequestBody DeviceDtos.AckCommandRequest request) {
        deviceService.acknowledgeCommand(commandId, request.getDurationSeconds(), request.getStatus());
        return ResponseEntity.ok().build();
    }
}


