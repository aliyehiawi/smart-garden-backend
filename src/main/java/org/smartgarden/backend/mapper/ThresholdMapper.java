package org.smartgarden.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.smartgarden.backend.config.MapStructConfig;
import org.smartgarden.backend.dto.ThresholdDtos;
import org.smartgarden.backend.entity.Threshold;

@Mapper(config = MapStructConfig.class)
public interface ThresholdMapper {
    @Mapping(source = "garden.id", target = "gardenId")
    @Mapping(source = "sensorType", target = "sensorType")
    ThresholdDtos.ThresholdResponse toResponse(Threshold threshold);
}

