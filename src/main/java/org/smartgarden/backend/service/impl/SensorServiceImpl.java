package org.smartgarden.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.dto.SensorDtos;
import org.smartgarden.backend.entity.ComparatorType;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.InitiatedBy;
import org.smartgarden.backend.entity.SensorData;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.exception.NotFoundException;
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
    private final PumpService pumpService;

    public SensorServiceImpl(DeviceRepository deviceRepository,
                             SensorDataRepository sensorDataRepository,
                             ThresholdRepository thresholdRepository,
                             PumpService pumpService) {
        this.deviceRepository = deviceRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.thresholdRepository = thresholdRepository;
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
            boolean trigger = (th.getComparator() == ComparatorType.BELOW && data.getValue() < th.getThresholdValue())
                    || (th.getComparator() == ComparatorType.ABOVE && data.getValue() > th.getThresholdValue());
            if (trigger && th.isAutoWaterEnabled()) {
                log.info("auto-watering-trigger gardenId={} type={} value={}", device.getGarden().getId(), type, data.getValue());
                pumpService.startPump(device.getGarden().getId(), th.getPumpMaxSeconds(), InitiatedBy.AUTO);
            }
        });
    }
}


