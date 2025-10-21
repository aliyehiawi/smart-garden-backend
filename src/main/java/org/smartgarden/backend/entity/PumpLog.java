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
public class PumpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Garden garden;

    @Column(nullable = false, length = 64)
    private String deviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PumpAction action;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private Integer durationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private InitiatedBy initiatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PumpStatus status;
}


