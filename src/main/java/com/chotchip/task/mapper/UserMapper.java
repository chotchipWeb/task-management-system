package com.chotchip.task.mapper;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.dto.response.UserResponseDTO;
import com.chotchip.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", expression = "java(com.chotchip.task.entity.enums.Role.CLIENT)")
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toDTO(User user);

}
