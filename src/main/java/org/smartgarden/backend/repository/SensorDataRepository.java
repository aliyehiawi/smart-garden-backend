package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.SensorData;
import org.smartgarden.backend.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    Page<SensorData> findByDeviceAndTimestampBetween(Device device, LocalDateTime from, LocalDateTime to, Pageable pageable);
}


