package org.smartgarden.backend.service;

import org.smartgarden.backend.entity.InitiatedBy;

public interface PumpService {
    void startPump(Long gardenId, Integer durationSeconds, InitiatedBy initiatedBy);
    void stopPump(Long gardenId, InitiatedBy initiatedBy);
}


