package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.PumpLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PumpLogRepository extends JpaRepository<PumpLog, Long> {
    List<PumpLog> findByGardenOrderByStartedAtDesc(Garden garden);
}


