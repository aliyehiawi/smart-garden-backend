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
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SensorType sensorType;

    @Column(name = "sensor_value", nullable = false)
    private Double value;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}


