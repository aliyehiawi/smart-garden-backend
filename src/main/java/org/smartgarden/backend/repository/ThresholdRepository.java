package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.SensorType;
import org.smartgarden.backend.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    Optional<Threshold> findByGardenAndSensorType(Garden garden, SensorType sensorType);
}


