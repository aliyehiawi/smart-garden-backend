package org.smartgarden.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.smartgarden.backend.entity.UserRole;
import org.smartgarden.backend.validation.ValidEnum;

public class UserDtos {
    @Data
    public static class UserRequest {
        @NotBlank
        @Size(min = 3, max = 64)
        private String username;
        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
        @NotBlank
        @ValidEnum(enumClass = UserRole.class)
        private String role;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String username;
        private String role;
    }
}
