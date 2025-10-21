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
public class DeviceCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Garden garden;

    @ManyToOne(optional = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PumpAction action;

    private Integer durationSeconds;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean acknowledged;

    private Integer actualDurationSeconds;

    @Enumerated(EnumType.STRING)
    private PumpStatus resultStatus;
}


