package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceId(String deviceId);
    java.util.List<Device> findByGarden_Id(Long gardenId);
}


