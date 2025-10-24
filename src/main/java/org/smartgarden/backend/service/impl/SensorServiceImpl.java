package org.smartgarden.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.dto.SensorDtos;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.InitiatedBy;
import org.smartgarden.backend.entity.PumpAction;
import org.smartgarden.backend.entity.SensorData;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.DeviceCommandRepository;
import org.smartgarden.backend.repository.DeviceRepository;
import org.smartgarden.backend.repository.SensorDataRepository;
import org.smartgarden.backend.repository.ThresholdRepository;
import org.smartgarden.backend.service.PumpService;
import org.smartgarden.backend.service.SensorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SensorServiceImpl implements SensorService {

    private final DeviceRepository deviceRepository;
    private final SensorDataRepository sensorDataRepository;
    private final ThresholdRepository thresholdRepository;
    private final DeviceCommandRepository deviceCommandRepository;
    private final PumpService pumpService;

    public SensorServiceImpl(DeviceRepository deviceRepository,
                             SensorDataRepository sensorDataRepository,
                             ThresholdRepository thresholdRepository,
                             DeviceCommandRepository deviceCommandRepository,
                             PumpService pumpService) {
        this.deviceRepository = deviceRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.thresholdRepository = thresholdRepository;
        this.deviceCommandRepository = deviceCommandRepository;
        this.pumpService = pumpService;
    }

    @Override
    @Transactional
    public void saveReading(String deviceId, SensorDtos.SensorDataRequest request) {
        Device device = deviceRepository.findByDeviceId(deviceId).orElseThrow(() -> new NotFoundException("Device not found"));
        LocalDateTime ts = request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now();
        SensorType type = SensorType.valueOf(request.getSensorType());
        SensorData data = SensorData.builder()
                .device(device)
                .sensorType(type)
                .value(request.getValue())
                .timestamp(ts)
                .build();
        sensorDataRepository.save(data);
        device.setLastSeen(LocalDateTime.now());

        thresholdRepository.findByGardenAndSensorType(device.getGarden(), type).ifPresent(th -> {
            if (!th.isAutoWaterEnabled()) {
                return;
            }

            double sensorValue = data.getValue();
            LocalDateTime recentWindow = LocalDateTime.now().minusMinutes(15);
            boolean isPumpRunning = deviceCommandRepository.existsByGardenAndActionAndAcknowledgedFalseAndCreatedAtAfter(
                    device.getGarden(), PumpAction.START, recentWindow);

            if (sensorValue < th.getMinThresholdValue() && !isPumpRunning) {
                log.info("auto-watering-start gardenId={} type={} value={} minThreshold={}", 
                        device.getGarden().getId(), type, sensorValue, th.getMinThresholdValue());
                pumpService.startPump(device.getGarden().getId(), th.getPumpMaxSeconds(), InitiatedBy.AUTO);
            } else if (sensorValue >= th.getMaxThresholdValue() && isPumpRunning) {
                log.info("auto-watering-stop gardenId={} type={} value={} maxThreshold={}", 
                        device.getGarden().getId(), type, sensorValue, th.getMaxThresholdValue());
                pumpService.stopPump(device.getGarden().getId(), InitiatedBy.AUTO);
            }
        });
    }
}


