package org.smartgarden.backend.service;

import org.smartgarden.backend.entity.Garden;

import java.util.List;

public interface GardenService {
    Garden createGarden(String name, String description, String location);
    List<Garden> listGardensForUser(String username);
    Garden getGarden(Long id);
}


