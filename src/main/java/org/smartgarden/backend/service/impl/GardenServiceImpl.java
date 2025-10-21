package org.smartgarden.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartgarden.backend.entity.Garden;
import org.smartgarden.backend.entity.User;
import org.smartgarden.backend.entity.UserRole;
import org.smartgarden.backend.exception.NotFoundException;
import org.smartgarden.backend.repository.GardenRepository;
import org.smartgarden.backend.repository.UserRepository;
import org.smartgarden.backend.service.GardenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final GardenRepository gardenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Garden createGarden(String name, String description, String location) {
        Garden g = Garden.builder().name(name).description(description).location(location).build();
        return gardenRepository.save(g);
    }

    @Override
    public List<Garden> listGardensForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getRole() == UserRole.ADMIN) {
            return gardenRepository.findAll();
        }
        return user.getAssignedGardens().stream().toList();
    }

    @Override
    public Garden getGarden(Long id) {
        return gardenRepository.findById(id).orElseThrow(() -> new NotFoundException("Garden not found"));
    }
}


