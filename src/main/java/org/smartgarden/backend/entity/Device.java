package org.smartgarden.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String deviceId;

    @Column(nullable = false, length = 128)
    private String apiKey;

    @ManyToOne(optional = false)
    private Garden garden;

    private LocalDateTime lastSeen;

    @Column(nullable = false)
    private boolean enabled;
}


