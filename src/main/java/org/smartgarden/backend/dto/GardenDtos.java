package org.smartgarden.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class GardenDtos {
    @Data
    public static class GardenRequest {
        @NotBlank
        @Size(max = 100)
        private String name;
        @Size(max = 255)
        private String description;
        @Size(max = 255)
        private String location;
    }

    @Data
    public static class GardenResponse {
        private Long id;
        private String name;
        private String description;
        private String location;
    }
}


