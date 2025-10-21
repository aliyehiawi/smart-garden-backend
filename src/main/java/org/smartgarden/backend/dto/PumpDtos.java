package org.smartgarden.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

public class PumpDtos {
    @Data
    public static class PumpStartRequest {
        @Min(1)
        @Max(600)
        private Integer durationSeconds;
    }
}


