package org.smartgarden.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.dto.DeviceDtos;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.DeviceCommand;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.mapper.DeviceMapper;
import org.smartgarden.backend.repository.DeviceCommandRepository;
import org.smartgarden.backend.repository.DeviceRepository;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final GardenRepository gardenRepository;
    private final DeviceCommandRepository deviceCommandRepository;
    private final DeviceMapper deviceMapper;
    private final SecureRandom secureRandom;

    public DeviceServiceImpl(DeviceRepository deviceRepository,
                             GardenRepository gardenRepository,
                             DeviceCommandRepository deviceCommandRepository,
                             DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.gardenRepository = gardenRepository;
        this.deviceCommandRepository = deviceCommandRepository;
        this.deviceMapper = deviceMapper;
        this.secureRandom = new SecureRandom();
    }

    @Override
    @Transactional
    public DeviceDtos.DeviceResponse registerDevice(DeviceDtos.DeviceRequest request) {
        Garden garden = gardenRepository.findById(request.getGardenId()).orElseThrow(() -> new NotFoundException("Garden not found"));
        String apiKey = generateApiKey();
        Device device = Device.builder()
                .deviceId(request.getDeviceId())
                .apiKey(apiKey)
                .garden(garden)
                .enabled(true)
                .build();
        deviceRepository.save(device);
        log.info("device-registered deviceId={} gardenId={}", device.getDeviceId(), garden.getId());
        return deviceMapper.toResponse(device);
    }

    @Override
    public List<DeviceCommand> getPendingCommands(String deviceId) {
        Device device = deviceRepository.findByDeviceId(deviceId).orElseThrow(() -> new NotFoundException("Device not found"));
        return deviceCommandRepository.findByDeviceAndAcknowledgedFalseOrderByCreatedAtAsc(device);
    }

    @Override
    @Transactional
    public void acknowledgeCommand(Long commandId, Integer durationSeconds, String status) {
        DeviceCommand cmd = deviceCommandRepository.findById(commandId).orElseThrow(() -> new NotFoundException("Command not found"));
        cmd.setAcknowledged(true);
        cmd.setActualDurationSeconds(durationSeconds);
        cmd.setResultStatus(org.smartgarden.backend.entity.PumpStatus.valueOf(status));
        deviceCommandRepository.save(cmd);
    }

    private String generateApiKey() {
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}


