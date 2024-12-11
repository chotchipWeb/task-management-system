package com.chotchip.task.mapper;

import com.chotchip.task.dto.response.CommentResponseDTO;
import com.chotchip.task.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "author", expression = "java(new com.chotchip.task.dto.response.UserResponseTaskDTO(comment.getAuthor().getEmail()))")
    CommentResponseDTO toDTO(Comment comment);

}
