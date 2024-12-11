package com.chotchip.task.mapper;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "role",expression = "java(com.chotchip.task.entity.enums.Role.CLIENT)")
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);
}
