package org.smartgarden.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.smartgarden.backend.config.MapStructConfig;
import org.smartgarden.backend.dto.DeviceDtos;
import org.smartgarden.backend.entity.Device;

@Mapper(config = MapStructConfig.class)
public interface DeviceMapper {
    @Mapping(target = "gardenId", source = "garden.id")
    DeviceDtos.DeviceResponse toResponse(Device device);
}


