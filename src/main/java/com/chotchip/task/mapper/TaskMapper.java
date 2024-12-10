package com.chotchip.task.mapper;

import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "author", expression = "java(new UserResponseTaskDTO(task.getAuthor().getEmail()))")
    @Mapping(target = "executor", expression = "java(new UserResponseTaskDTO(task.getExecutor().getEmail()))")
    TaskResponseDTO toDTO(Task task);


}
