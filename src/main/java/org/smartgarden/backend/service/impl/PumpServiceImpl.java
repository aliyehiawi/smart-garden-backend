package org.smartgarden.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.DeviceCommand;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.InitiatedBy;
import org.smartgarden.backend.entity.PumpAction;
import org.smartgarden.backend.entity.PumpLog;
import org.smartgarden.backend.entity.PumpStatus;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.entity.Threshold;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.DeviceCommandRepository;
import org.smartgarden.backend.repository.DeviceRepository;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.PumpLogRepository;
import org.smartgarden.backend.repository.ThresholdRepository;
import org.smartgarden.backend.service.PumpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PumpServiceImpl implements PumpService {

    private final GardenRepository gardenRepository;
    private final ThresholdRepository thresholdRepository;
    private final PumpLogRepository pumpLogRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceCommandRepository deviceCommandRepository;

    public PumpServiceImpl(GardenRepository gardenRepository,
                           ThresholdRepository thresholdRepository,
                           PumpLogRepository pumpLogRepository,
                           DeviceRepository deviceRepository,
                           DeviceCommandRepository deviceCommandRepository) {
        this.gardenRepository = gardenRepository;
        this.thresholdRepository = thresholdRepository;
        this.pumpLogRepository = pumpLogRepository;
        this.deviceRepository = deviceRepository;
        this.deviceCommandRepository = deviceCommandRepository;
    }

    @Override
    @Transactional
    public void startPump(Long gardenId, Integer durationSeconds, InitiatedBy initiatedBy) {
        Garden garden = gardenRepository.findById(gardenId).orElseThrow(() -> new NotFoundException("Garden not found"));
        int max = thresholdRepository.findByGardenAndSensorType(garden, SensorType.SOIL_MOISTURE)
                .map(Threshold::getPumpMaxSeconds).orElse(60);
        int duration = durationSeconds != null ? Math.min(durationSeconds, max) : max;

        log.info("pump-start gardenId={} durationSeconds={} initiatedBy={}", gardenId, duration, initiatedBy);
        PumpLog logEntry = PumpLog.builder()
                .garden(garden)
                .deviceId("broadcast")
                .action(PumpAction.START)
                .startedAt(LocalDateTime.now())
                .durationSeconds(duration)
                .initiatedBy(initiatedBy)
                .status(PumpStatus.SUCCESS)
                .build();
        pumpLogRepository.save(logEntry);

        for (Device d : deviceRepository.findByGarden_Id(gardenId)) {
            DeviceCommand cmd = DeviceCommand.builder()
                    .garden(garden)
                    .device(d)
                    .action(PumpAction.START)
                    .durationSeconds(duration)
                    .createdAt(LocalDateTime.now())
                    .acknowledged(false)
                    .build();
            deviceCommandRepository.save(cmd);
        }
    }

    @Override
    @Transactional
    public void stopPump(Long gardenId, InitiatedBy initiatedBy) {
        Garden garden = gardenRepository.findById(gardenId).orElseThrow(() -> new NotFoundException("Garden not found"));
        log.info("pump-stop gardenId={} initiatedBy={}", gardenId, initiatedBy);
        PumpLog logEntry = PumpLog.builder()
                .garden(garden)
                .deviceId("broadcast")
                .action(PumpAction.STOP)
                .startedAt(LocalDateTime.now())
                .initiatedBy(initiatedBy)
                .status(PumpStatus.SUCCESS)
                .build();
        pumpLogRepository.save(logEntry);

        for (Device d : deviceRepository.findByGarden_Id(gardenId)) {
            DeviceCommand cmd = DeviceCommand.builder()
                    .garden(garden)
                    .device(d)
                    .action(PumpAction.STOP)
                    .createdAt(LocalDateTime.now())
                    .acknowledged(false)
                    .build();
            deviceCommandRepository.save(cmd);
        }
    }
}


