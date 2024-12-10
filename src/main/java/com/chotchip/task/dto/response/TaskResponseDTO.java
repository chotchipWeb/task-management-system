package com.chotchip.task.dto.response;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.dto.request.CommentCreateRequestDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.User;
import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private String title;
    private String details;
    private Status status;
    private Priority priority;
    private List<CommentResponseDTO> comment;
    private UserResponseTaskDTO author;
    private UserResponseTaskDTO executor;
}
