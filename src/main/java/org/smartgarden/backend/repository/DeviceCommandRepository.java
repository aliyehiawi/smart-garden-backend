package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.entity.DeviceCommand;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.PumpAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, Long> {
    List<DeviceCommand> findByDeviceAndAcknowledgedFalseOrderByCreatedAtAsc(Device device);
    
    @Query("SELECT CASE WHEN COUNT(dc) > 0 THEN true ELSE false END FROM DeviceCommand dc "
           + "WHERE dc.garden = :garden AND dc.action = :action AND dc.acknowledged = false "
           + "AND dc.createdAt > :since")
    boolean existsByGardenAndActionAndAcknowledgedFalseAndCreatedAtAfter(
            @Param("garden") Garden garden, 
            @Param("action") PumpAction action, 
            @Param("since") LocalDateTime since);
}


