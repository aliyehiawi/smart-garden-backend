package org.smartgarden.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.smartgarden.backend.entity.ComparatorType;
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
        private Double thresholdValue;
        @NotNull
        @ValidEnum(enumClass = ComparatorType.class)
        private String comparator;
        @NotNull
        private Boolean autoWaterEnabled;
        @Min(1)
        @Max(600)
        private Integer pumpMaxSeconds;
    }
}


