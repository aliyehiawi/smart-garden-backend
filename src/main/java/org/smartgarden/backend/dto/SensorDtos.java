package org.smartgarden.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public class SensorDtos {
    @Data
    public static class SensorDataRequest {
        @NotBlank
        private String sensorType;
        @NotNull
        private Double value;
        private LocalDateTime timestamp;
    }
}


