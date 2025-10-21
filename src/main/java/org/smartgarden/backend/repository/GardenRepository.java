package org.smartgarden.backend.repository;

import org.smartgarden.backend.entity.Garden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GardenRepository extends JpaRepository<Garden, Long> {
}


