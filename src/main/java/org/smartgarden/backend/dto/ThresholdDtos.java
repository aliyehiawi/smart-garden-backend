package org.smartgarden.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.validation.ValidEnum;

public class ThresholdDtos {
    @Data
    public static class ThresholdRequest {
        @NotNull
        private Long gardenId;
        @NotNull
        @ValidEnum(enumClass = SensorType.class)
        private String sensorType;
        @NotNull
        private Double minThresholdValue;
        @NotNull
        private Double maxThresholdValue;
        @NotNull
        private Boolean autoWaterEnabled;
        @Min(1)
        @Max(600)
        private Integer pumpMaxSeconds;
    }

    @Data
    public static class ThresholdResponse {
        private Long id;
        private Long gardenId;
        private String sensorType;
        private Double minThresholdValue;
        private Double maxThresholdValue;
        private boolean autoWaterEnabled;
        private int pumpMaxSeconds;
    }
}


