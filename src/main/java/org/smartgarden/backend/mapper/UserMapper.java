package org.smartgarden.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.smartgarden.backend.config.MapStructConfig;
import org.smartgarden.backend.dto.UserDtos;
import org.smartgarden.backend.entity.User;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserDtos.UserResponse toResponse(User user);
}


