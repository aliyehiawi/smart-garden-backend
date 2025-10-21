package org.smartgarden.backend.mapper;

import org.mapstruct.Mapper;
import org.smartgarden.backend.config.MapStructConfig;
import org.smartgarden.backend.dto.GardenDtos;
import org.smartgarden.backend.entity.Garden;

@Mapper(config = MapStructConfig.class)
public interface GardenMapper {
    GardenDtos.GardenResponse toResponse(Garden garden);
}


