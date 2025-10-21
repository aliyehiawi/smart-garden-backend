package org.smartgarden.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.entity.*;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.ThresholdRepository;
import org.smartgarden.backend.service.ThresholdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThresholdServiceImpl implements ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final GardenRepository gardenRepository;

    @Override
    @Transactional
    public Threshold upsertThreshold(ThresholdDtos.ThresholdRequest request) {
        Garden garden = gardenRepository.findById(request.getGardenId()).orElseThrow(() -> new NotFoundException("Garden not found"));
        SensorType sensorType = SensorType.valueOf(request.getSensorType());
        ComparatorType comparatorType = ComparatorType.valueOf(request.getComparator());
        
        Threshold threshold = thresholdRepository.findByGardenAndSensorType(garden, sensorType)
                .orElse(Threshold.builder().garden(garden).sensorType(sensorType).build());
        threshold.setThresholdValue(request.getThresholdValue());
        threshold.setComparator(comparatorType);
        threshold.setAutoWaterEnabled(Boolean.TRUE.equals(request.getAutoWaterEnabled()));
        threshold.setPumpMaxSeconds(request.getPumpMaxSeconds() != null ? request.getPumpMaxSeconds() : 60);
        return thresholdRepository.save(threshold);
    }
}


