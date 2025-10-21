package org.smartgarden.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.smartgarden.backend.entity.PumpStatus;
import org.smartgarden.backend.validation.ValidEnum;

public class DeviceDtos {
    @Data
    public static class DeviceRequest {
        @NotBlank
        private String deviceId;
        @NotNull
        private Long gardenId;
    }

    @Data
    public static class DeviceResponse {
        private Long id;
        private String deviceId;
        private String apiKey;
        private Long gardenId;
        private boolean enabled;
    }

    @Data
    public static class AckCommandRequest {
        private Integer durationSeconds;
        @NotBlank
        @ValidEnum(enumClass = PumpStatus.class)
        private String status;
    }
}


