package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.DeviceCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, Long> {
    List<DeviceCommand> findByDeviceAndAcknowledgedFalseOrderByCreatedAtAsc(Device device);
}


