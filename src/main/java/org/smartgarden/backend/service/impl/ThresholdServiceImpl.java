package org.smartgarden.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.entity.Threshold;
import org.smartgarden.backend.exception.BadRequestException;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.ThresholdRepository;
import org.smartgarden.backend.service.ThresholdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThresholdServiceImpl implements ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final GardenRepository gardenRepository;

    @Override
    @Transactional
    public Threshold upsertThreshold(ThresholdDtos.ThresholdRequest request) {
        Garden garden = gardenRepository.findById(request.getGardenId())
                .orElseThrow(() -> new NotFoundException("Garden not found"));
        SensorType sensorType = SensorType.valueOf(request.getSensorType());
        
        if (request.getMinThresholdValue() >= request.getMaxThresholdValue()) {
            throw new BadRequestException("Min threshold must be less than max threshold");
        }
        
        Threshold threshold = thresholdRepository.findByGardenAndSensorType(garden, sensorType)
                .orElse(Threshold.builder().garden(garden).sensorType(sensorType).build());
        threshold.setMinThresholdValue(request.getMinThresholdValue());
        threshold.setMaxThresholdValue(request.getMaxThresholdValue());
        threshold.setAutoWaterEnabled(Boolean.TRUE.equals(request.getAutoWaterEnabled()));
        threshold.setPumpMaxSeconds(request.getPumpMaxSeconds() != null ? request.getPumpMaxSeconds() : 60);
        return thresholdRepository.save(threshold);
    }

    @Override
    public List<Threshold> getThresholdsByGarden(Long gardenId) {
        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new NotFoundException("Garden not found"));
        return thresholdRepository.findByGarden(garden);
    }

    @Override
    public Threshold getThresholdByGardenAndSensor(Long gardenId, String sensorType) {
        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new NotFoundException("Garden not found"));
        SensorType type = SensorType.valueOf(sensorType);
        return thresholdRepository.findByGardenAndSensorType(garden, type)
                .orElseThrow(() -> new NotFoundException("Threshold not found for sensor type: " + sensorType));
    }
}


