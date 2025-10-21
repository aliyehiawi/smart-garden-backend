package org.smartgarden.backend.service;

import org.smartgarden.backend.dto.DeviceDtos;
import org.smartgarden.backend.entity.DeviceCommand;

import java.util.List;

public interface DeviceService {
    DeviceDtos.DeviceResponse registerDevice(DeviceDtos.DeviceRequest request);
    List<DeviceCommand> getPendingCommands(String deviceId);
    void acknowledgeCommand(Long commandId, Integer durationSeconds, String status);
}


