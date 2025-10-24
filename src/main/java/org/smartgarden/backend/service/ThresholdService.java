package org.smartgarden.backend.service;

import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.entity.Threshold;

import java.util.List;

public interface ThresholdService {
    Threshold upsertThreshold(ThresholdDtos.ThresholdRequest request);
    List<Threshold> getThresholdsByGarden(Long gardenId);
    Threshold getThresholdByGardenAndSensor(Long gardenId, String sensorType);
}


