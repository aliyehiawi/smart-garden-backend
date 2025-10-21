package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Alert;
import org.smartgarden.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserOrderByTimestampDesc(User user);
}


