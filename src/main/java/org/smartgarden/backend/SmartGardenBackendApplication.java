package org.smartgarden.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class SmartGardenBackendApplication {

    private SmartGardenBackendApplication() {
        // Private constructor to prevent instantiation
    }

    public static void main(String[] args) {
        SpringApplication.run(SmartGardenBackendApplication.class, args);
    }
}
