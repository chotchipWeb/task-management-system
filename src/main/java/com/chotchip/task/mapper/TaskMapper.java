package com.chotchip.task.mapper;

import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.dto.response.UserResponseTaskDTO;
import com.chotchip.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "author", expression = "java(new com.chotchip.task.dto.response.UserResponseTaskDTO(task.getAuthor().getEmail()))")
    @Mapping(target = "executor", expression = "java(new com.chotchip.task.dto.response.UserResponseTaskDTO(task.getExecutor().getEmail()))")
    TaskResponseDTO toDTO(Task task);

    @Mapping(target = "executor" ,ignore = true)
    @Mapping(target = "status", expression = "java(com.chotchip.task.entity.enums.Status.PENDING)")
    Task toEntity(TaskCreateRequestDTO dto);
}
