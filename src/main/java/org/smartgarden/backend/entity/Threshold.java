package org.smartgarden.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Threshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Garden garden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SensorType sensorType;

    @Column(nullable = false)
    private Double thresholdValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ComparatorType comparator;

    @Column(nullable = false)
    private boolean autoWaterEnabled;

    @Column(nullable = false)
    private int pumpMaxSeconds;
}


