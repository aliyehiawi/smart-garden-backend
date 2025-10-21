package org.smartgarden.backend.service;

import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.entity.Threshold;

public interface ThresholdService {
    Threshold upsertThreshold(ThresholdDtos.ThresholdRequest request);
}


