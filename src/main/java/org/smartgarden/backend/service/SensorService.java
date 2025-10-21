package org.smartgarden.backend.service;

import org.smartgarden.backend.dto.SensorDtos;

public interface SensorService {
    void saveReading(String deviceId, SensorDtos.SensorDataRequest request);
}


